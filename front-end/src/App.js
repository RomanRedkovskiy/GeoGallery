import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import StartPage from './Unauthorized/StartPage';
import RedirectingComponent from './Unauthorized/RedirectingComponent';
import ProfilePage from './User/ProfilePage';
import AdminPage from './Admin/AdminPage';
import ProcessUserDelete from './Admin/ProcessUserDelete';
import ProcessNewImageBind from './User/ProcessNewImageBind';
import ImageList from './User/ImageList';
import ProcessImageDelete from './User/ProcessImageDelete';
import ProcessCommentAdd from './User/ProcessCommentAdd'
import ImageDetails from './User/ImageDetails';
import Login from './Unauthorized/Login';
import Registration from './Unauthorized/Registration';
import ProcessCommentDelete from './User/ProcessCommentDelete';


function App() {
  return (
	<Router>
		<Switch>
			<Route exact path="/">
				<StartPage />
			</Route>
			<Route exact path="/redirecting">
				<RedirectingComponent />
			</Route>
			<Route exact path="/profile">
				<ProfilePage />
			</Route>
			<Route exact path="/admin">
				<AdminPage />
			</Route>
			<Route exact path="/delete-user">
				<ProcessUserDelete />
			</Route>
			<Route exact path="/new-image">
				<ProcessNewImageBind />
			</Route>
			<Route exact path="/delete-image">
				<ProcessImageDelete/>
			</Route>
			<Route exact path="/image-details">
				<ImageDetails/>
			</Route>
			<Route exact path="/add-comment">
				<ProcessCommentAdd/>
			</Route>
			<Route exact path="/delete-comment">
				<ProcessCommentDelete/>
			</Route>
			<Route exact path="/images">
				<ImageList/>
			</Route>
			<Route exact path="/registration">
				<Registration />
			</Route>
			<Route exact path="/login">
				<Login />
			</Route>
		</Switch>
	</Router>
  );
}

export default App;
