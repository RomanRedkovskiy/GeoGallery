package com.example.userservice.repository;

import com.example.userservice.dto.userDto.UserCredentialsDto;
import com.example.userservice.dto.userDto.UserGeneralDto;
import com.example.userservice.dto.userDto.UserIndexedDto;
import com.example.userservice.dto.userDto.UserRegistrationDto;
import com.example.userservice.model.ImageBind;
import com.example.userservice.util.CipherProvider;
import com.example.userservice.util.exceptions.AuthenticationExecutionException;
import com.example.userservice.util.exceptions.FirebaseStorageException;
import com.example.userservice.util.exceptions.RealtimeDatabaseException;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@Component
public class UserRepositoryImpl implements UserRepository {

    @Value("${secret.claim}")
    String claimName;

    @Value("${firebase.storage}")
    String firebaseStorage;

    private final CipherProvider cipherProvider;

    private final Storage storage;

    @Autowired
    public UserRepositoryImpl(CipherProvider cipherProvider, Storage storage) {
        this.cipherProvider = cipherProvider;
        this.storage = storage;
    }

    public CompletableFuture<UserIndexedDto> registerUser(UserRegistrationDto user) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        CompletableFuture<UserIndexedDto> futureResult = new CompletableFuture<>();

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(user.getLogin())
                .setPassword(user.getPassword());

        ApiFuture<UserRecord> apiFuture = firebaseAuth.createUserAsync(request);

        ApiFutures.addCallback(apiFuture, new ApiFutureCallback<>() {
            @Override
            public void onSuccess(UserRecord userRecord) {
                setCustomClaims(firebaseAuth, userRecord.getUid(), user.getPassword());
                futureResult.complete(new UserIndexedDto(userRecord.getUid(), user.getName(), null));
            }

            @Override
            public void onFailure(Throwable throwable) {
                futureResult.completeExceptionally(throwable);
            }
        }, MoreExecutors.directExecutor());

        return futureResult;
    }

    @Override
    public void addUserJwtToDatabase(String id, String jwt) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Jwts");
        try {
            databaseRef.child(id).setValueAsync(jwt);
        } catch (DatabaseException e) {
            throw new RealtimeDatabaseException(e.getMessage());
        }
    }

    @Override
    public void deleteUserJwtFromDatabase(String id) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Jwts");
        databaseRef.child(id).removeValueAsync();
    }

    @Override
    public void deleteImageCommentsFromDatabase(String imagePath) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Comments");
        databaseRef.child(imagePath).removeValueAsync();
    }

    @Override
    public void deleteUserFromDatabase(String id) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        databaseRef.child(id).removeValueAsync();
    }

    @Override
    public void addUserToDatabase(String id, UserGeneralDto user) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");

        try {
            databaseRef.child(id).setValueAsync(user);
        } catch (DatabaseException e) {
            throw new RealtimeDatabaseException(e.getMessage());
        }
    }

    @Override
    public void addFileToStorage(MultipartFile file, String filePath) {
        try {
            byte[] fileContent = file.getBytes();
            BlobId blobId = BlobId.of(firebaseStorage, filePath);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            storage.create(blobInfo, fileContent);
        } catch (IOException e) {
            throw new FirebaseStorageException(e.getMessage());
        }
    }

    //to retrieve image by url in React
    @Override
    public String getFileUrlFromStorage(String filePath) {
        Blob blob = storage.get(BlobId.of(firebaseStorage, filePath));
        return blob.signUrl(1, TimeUnit.HOURS).toString(); //link will be accessible for 1 hour only
    }

    //CompletableFuture will be handled in UserService
    @Override
    public CompletableFuture<UserIndexedDto> getUserFromDatabase(String id) {
        CompletableFuture<UserIndexedDto> userFuture = new CompletableFuture<>();

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        databaseRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserIndexedDto user = dataSnapshotToUserDto(dataSnapshot);
                    userFuture.complete(user);
                } else {
                    userFuture.completeExceptionally(new RealtimeDatabaseException("User wasn't found in the database"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error occurred while retrieving data
                userFuture.completeExceptionally(new RealtimeDatabaseException("Error retrieving user data: " + databaseError.getMessage()));
            }
        });

        return userFuture;
    }

    //for admin purposes
    //CompletableFuture will be handled in UserService
    @Override
    public CompletableFuture<List<UserIndexedDto>> getAllUsersFromDatabase() {
        CompletableFuture<List<UserIndexedDto>> usersFuture = new CompletableFuture<>();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<UserIndexedDto> users = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        UserIndexedDto user = dataSnapshotToUserDto(userSnapshot);
                        users.add(user);
                    }
                }
                usersFuture.complete(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error occurred while retrieving data
                usersFuture.completeExceptionally(new RealtimeDatabaseException("Error retrieving user data: " + databaseError.getMessage()));
            }
        });

        return usersFuture;
    }

    //to choose jwt token with which role will be sent as a response
    //CompletableFuture will be handled in UserService
    @Override
    public CompletableFuture<Boolean> isUserAdmin(String id) {
        CompletableFuture<Boolean> isAdminFuture = new CompletableFuture<>();

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        databaseRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if(dataSnapshot.hasChild("isAdmin")){
                        isAdminFuture.complete(dataSnapshot.child("isAdmin").getValue(Boolean.class));
                    } else {
                        isAdminFuture.complete(Boolean.FALSE);
                    }
                } else {
                    isAdminFuture.completeExceptionally(new RealtimeDatabaseException("User wasn't found in the database"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error occurred while retrieving data
                isAdminFuture.completeExceptionally(new RealtimeDatabaseException("Error retrieving user data: " + databaseError.getMessage()));
            }
        });
        return isAdminFuture;
    }

    //CompletableFuture will be handled in UserService
    @Override
    public CompletableFuture<UserIndexedDto> loginUser(UserCredentialsDto user) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        UserRecord userRecord;
        try {
            userRecord = auth.getUserByEmail(user.getLogin());
        } catch (FirebaseAuthException e) {
            throw new AuthenticationExecutionException(e.getMessage());
        }
        if (!isPasswordCorrect(user.getPassword(), userRecord)) {
            throw new AuthenticationExecutionException("Incorrect password");
        }

        return getUserFromDatabase(userRecord.getUid());
    }

    @Override
    public void deleteFileFromStorage(String filePath) {
        BlobId blobId = BlobId.of(firebaseStorage, filePath);
        storage.delete(blobId);
    }

    @Override
    public void deleteUserFromAuthenticate(String id) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        try {
            auth.deleteUser(id);
        } catch (FirebaseAuthException e) {
            throw new AuthenticationExecutionException(e.getMessage());
        }
    }

    //transform dataSnapshot from FirebaseDatabase to UserIndexedDto object
    private UserIndexedDto dataSnapshotToUserDto(DataSnapshot dataSnapshot) {
        String name = dataSnapshot.child("name").getValue(String.class);
        List<ImageBind> images = new ArrayList<>();
        if (dataSnapshot.hasChild("images")) {
            DataSnapshot arraySnapshot = dataSnapshot.child("images");
            for (DataSnapshot imageSnapshot : arraySnapshot.getChildren()) {
                String imagePath = imageSnapshot.child("imagePath").getValue(String.class);
                Double latitude = imageSnapshot.child("latitude").getValue(Double.class);
                Double longitude = imageSnapshot.child("longitude").getValue(Double.class);

                ImageBind imageBind = new ImageBind(imagePath, latitude, longitude);
                images.add(imageBind);
            }
        }
        return new UserIndexedDto(dataSnapshot.getKey(), name, images);
    }

    private void setCustomClaims(FirebaseAuth firebaseAuth, String uid, String pass) {
        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put(claimName, cipherProvider.encrypt(pass));
        try {
            firebaseAuth.setCustomUserClaims(uid, customClaims);
        } catch (FirebaseAuthException e) {
            throw new AuthenticationExecutionException(e.getMessage());
        }
    }

    private boolean isPasswordCorrect(String pass, UserRecord userRecord) {
        return cipherProvider.encrypt(pass).equals(userRecord.getCustomClaims().get(claimName).toString());
    }
}
