import { Link } from 'react-router-dom'

const UnauthorizedNavBar = () => {
	return ( 
		<nav className="navbar">
			<Link to="/">
				<h1>GeoGallery</h1>
			</Link>
            <div className="links">
                <Link to="/login">Log in</Link>
                <Link to="/registration" style = {{
					color: "white",
					backgroundColor: '#f1365d',
					borderRadius: '8px'
				}}>Sign Up</Link>
            </div>
        </nav>
	 );
}
 
export default UnauthorizedNavBar;