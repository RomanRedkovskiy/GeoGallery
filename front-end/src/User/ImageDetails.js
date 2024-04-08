import { useState } from "react";
import { Link } from "react-router-dom/cjs/react-router-dom";
import useFetchData from "../fetches/useFetchData";
import AuthorizedNavbar from "../Navbars/NavbarAuthorized";

const ImageDetails = () => {
  
	function truncateTextWithEllipsis(text, length) {
		if (text.length <= length) {
			return text;
		}
		return text.substr(0, length) + '\u2026'
	}

	const {data: imageDetails, isLoading, error} =
		useFetchData('http://localhost:8080/images/details', 'POST', localStorage.getItem('imagePath'));

	function handleDeleteComment(id){
		localStorage.setItem('commentId', id);
	}

	const [inputValue, setInputValue] = useState('');

	const handleAddComment = () => {
	  localStorage.setItem('commentText', inputValue);
	  setInputValue('');
	};

	return ( 
	<>
		<AuthorizedNavbar/>
		<div style={{ display: 'flex', alignItems: 'center', marginTop: '1em' }}>
        	<input type="text" style={{ height: '2em', marginRight: '0.5em' }} 
				value={inputValue} onChange={(e) => setInputValue(e.target.value)}/>
			<Link onClick = {() => handleAddComment()} to = "/add-comment">
				<button style={{ height: '2em', width: '10em' }}>Add Comment</button>
			</Link>
      	</div>
		<div style={{ display: 'flex'}}>
		{imageDetails && imageDetails.length !== 0 &&
		<>
		<div style={{ flex: '0 0 auto', alignSelf: 'flex-start' }}>
      		<img src={imageDetails.fileUrl} alt="Image" style={{ maxWidth: '600px', maxHeight: '100%', marginBottom: '10px' }} />
    	</div>
		<div style={{ display: 'flex', flexDirection: 'column' }}>
			{imageDetails.comments.map((comment) => (
				<div key={comment.id} className="compilation-container compilation">
					<p>{comment.date}</p>
					<h2>{truncateTextWithEllipsis(comment.text, 25)}</h2>
					<Link onClick = {() => handleDeleteComment(comment.id)} to = "/delete-comment">
						<button>Delete</button>
					</Link>
				</div>
			))}
		</div>
		</>
		}
		</div>
	</> 
	);
}
 
export default ImageDetails;