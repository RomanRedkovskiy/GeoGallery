import NavbarAuthrorized from "../Navbars/NavbarAuthorized";
import useFetch from "../fetches/useFetch";
import { Link } from "react-router-dom";


const AdminPage = () => {

	const {data: userDetails, isLoading, error} =
		useFetch('http://localhost:8080/users');

	function setAuxiliaryId(userId){
		localStorage.setItem('auxiliaryId', userId);
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
		<NavbarAuthrorized/>
		<div className="list-preview">
		{userDetails &&
		userDetails.map((user) => (
			<div className = 'compilation-container compilation'>
			<Link onClick = {() => setAuxiliaryId(user.id)} to = "/images">
				<h2>{truncateTextWithEllipsis(truncatePathToName(user.name), 25)}</h2>
			</Link>
			<Link onClick = {() => setAuxiliaryId(user.id)} to = "/delete-user">
				{user.id !== localStorage.getItem('userId') &&<button type = "button" 
					className = "small-button delete-button" style = {{top: "10px"}}>
					<h2 style = {{backgroundColor: '#f2eded'}}>Delete</h2>
				</button>
				}
			</Link>	
			</div>
		))};
		</div>
	</>
	);
}
 
export default AdminPage;