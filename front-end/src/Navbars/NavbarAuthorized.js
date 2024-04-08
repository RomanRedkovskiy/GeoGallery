import { Link } from 'react-router-dom'

const AuthorizedNavbar = () => {

	const isAdmin = localStorage.getItem('isAdmin') === 'true';
	console.log(isAdmin);
	console.log('s');
	
	function handleLogout(){
		localStorage.clear();
	}

    return (
        <nav className="navbar">
			{isAdmin ? (
				<>
				<Link to="/admin"><h1>GeoGallery <strong><i>Admin</i></strong></h1></Link>
				<div className="links">
					<Link onClick = {handleLogout} to="/" style = {{
						color: "white",
						backgroundColor: '#f1365d',
						borderRadius: '8px'
					}}>Log out</Link>
            	</div>
				</>
			) : (
				<>
				<Link to="/profile"><h1>GeoGallery</h1></Link>
				<div className="links">
					<Link to = "/new-image">Add image</Link>
					<Link to = "/images">Images</Link>
					<Link onClick = {handleLogout} to="/" style = {{
						color: "white",
						backgroundColor: '#f1365d',
						borderRadius: '8px'
					}}>Log out</Link>
				</div>
				</>
			)}
        </nav>
     );
}
 
export default AuthorizedNavbar;