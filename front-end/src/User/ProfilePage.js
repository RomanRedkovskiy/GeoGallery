import AuthorizedNavBar from "../Navbars/NavbarAuthorized";
import "../leaflet.css";
import "leaflet/dist/leaflet.css";
import useFetch from "../fetches/useFetch";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import { Icon } from "leaflet";

const customIcon = new Icon({
	iconUrl: require("../Images/marker.png"),
	iconSize: [38, 38]
});

const ProfilePage = () => {

	const {data: imageBinds, isLoading, error} =
		useFetch('http://localhost:8080/users/' + localStorage.getItem('userId') + '/urls');

	return ( 
		<>
			<AuthorizedNavBar/>
			<MapContainer center={[48.8566, 2.3522]} zoom={13}>

			<TileLayer
				attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
				url="https://tile.openstreetmap.org/{z}/{x}/{y}.png"
			/>

			{imageBinds && imageBinds.map((imageBind) => (
				<Marker position={imageBind.position} icon={customIcon}>
					<Popup>
						<img src={imageBind.imageUrl} style={{ maxWidth: '200px' }} />
					</Popup>
				</Marker>
			))};

			</MapContainer>
			<div></div>
		</>
	);
}

 
export default ProfilePage;