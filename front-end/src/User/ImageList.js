import { Link } from "react-router-dom";
import useFetch from "../fetches/useFetch";
import AuthorizedNavbar from "../Navbars/NavbarAuthorized";

const ImageList = () => {

	const userId = localStorage.getItem('userId');
	const auxiliaryId = localStorage.getItem('auxiliaryId');
	const isAdmin = localStorage.getItem('isAdmin') === 'true';
	
	const url = isAdmin ? 'http://localhost:8080/users/' + auxiliaryId + '/paths' : 'http://localhost:8080/users/' + userId + '/paths';
	
	const { data: imageBinds, isLoading, error } = useFetch(url);

	function setImagePath(imagePath){
		localStorage.setItem('imagePath', imagePath);
	}

	function truncateTextWithEllipsis(text, length) {
		if (text.length <= length) {
			return text;
		}
		
		return text.substr(0, length) + '\u2026'
	}

	function truncatePathToName(str) {
		const firstSlashIndex = str.indexOf('/');
		const lastUnderscoreIndex = str.lastIndexOf('_');
		
		if (firstSlashIndex >= 0 && lastUnderscoreIndex >= 0 && lastUnderscoreIndex > firstSlashIndex) {
			const truncatedStr = str.substring(firstSlashIndex + 1, lastUnderscoreIndex);
			return truncatedStr;
		}
		
		return str;
	}

	return ( 
	<>
		<AuthorizedNavbar/>
		{imageBinds && imageBinds.length !== 0 &&
		<h2>Every Image with its name and geographical position. Click for deatils.</h2>}
		{(!imageBinds || imageBinds.length === 0) &&
		<h2>No images were found here...</h2>}
		<div className="list-preview">
			{imageBinds &&
			imageBinds.map((imageBind) => (
				<div className = 'compilation-container compilation'>
					<Link onClick = {() => setImagePath(imageBind.imagePath)} to = "/image-details">
						<h2>{truncateTextWithEllipsis(truncatePathToName(imageBind.imagePath), 25)}</h2>
						<h3>Latitude: {imageBind.position[0]}</h3>
						<h3>Longitude: {imageBind.position[1]}</h3>
					</Link>
					<Link onClick = {() => setImagePath(imageBind.imagePath)} to = "/delete-image">
						<button type = "button" className = "small-button delete-button" style = {{top: "40px"}}>
							<h2 style = {{backgroundColor: '#f2eded'}}>Delete</h2>
						</button>	
					</Link>		
				</div>
			))}
		</div>
	</> 
	);
}
 
export default ImageList;