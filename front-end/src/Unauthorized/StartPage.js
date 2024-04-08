import UnauthorizedNavBar from "../Navbars/NavbarUnauthorized";
import AboutProject from "./AboutProject";


const StartPage = () => {
	return ( 
		<div>
			<UnauthorizedNavBar />
			<div className="content">
				<AboutProject />
			</div>
		</div>
	 );
}
 
export default StartPage;