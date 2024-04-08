import AuthorizedNavBar from "../Navbars/NavbarAuthorized";
import React, { useState } from 'react';
import "../leaflet.css";
import "leaflet/dist/leaflet.css";
import { MapContainer, TileLayer, Marker, useMapEvents  } from "react-leaflet";
import { Icon } from "leaflet";
import { useHistory } from 'react-router-dom';
import { Link } from "react-router-dom/cjs/react-router-dom.min";

const ProcessNewImageBind = () => {

	const history = useHistory();
	const [errorMessage, setErrorMessage] = useState(null);

	const [isImageChosen, setIsImageChosen] = useState(false);
	const [isPositionChosen, setIsPositionChosen] = useState(false);
	const [file, setFile] = useState();

	const handleImageChange = (event) => {
		if(event.target.files){
			setFile(event.target.files[0]);
			const allowedExtensions = ['.jpg', '.jpeg', '.png', '.gif'];
			const fileExtension = getFileExtension(event.target.files[0].name);
		
			if (allowedExtensions.includes(fileExtension.toLowerCase())) {
				setIsImageChosen(true);
			} else {
				setIsImageChosen(false);
			}
		}
	  };
	
	  const getFileExtension = (filename) => {
			return filename.slice(filename.lastIndexOf('.'));
	  };

	const customIcon = new Icon({
		iconUrl: require("../Images/marker.png"),
		iconSize: [38, 38]
	});
	
	const [customPosition, setCustomPosition] = useState([0, 0]);
	
	const handleMapClick = (e) => {
		const { lat, lng } = e.latlng;
		console.log(`Clicked coordinates: Latitude: ${lat}, Longitude: ${lng}`);
		setCustomPosition([lat, lng]);
		setIsPositionChosen(true);
	};
	
	function MapClickHandler() {
		useMapEvents({
			click: handleMapClick,
		});
		return null;
	}

	const handleSendData = () => {
		if (file && customPosition) {
			const formData = new FormData();
			formData.append('pos', customPosition);
			formData.append('file', file);
			fetch('http://localhost:8080/users/' + localStorage.getItem('userId') + '/image', {
			  method: 'POST',
			  headers: {
				"Authorization": localStorage.getItem('Token'),
				"Access-Control-Allow-Headers": "Content-Type",
				"Access-Control-Allow-Origin": "*",
				"Access-Control-Allow-Methods": "OPTIONS,POST",
				"Cache-Control": "no-cache",
			  },
			  body: formData,
			})
			  .then(response => {
				// Handle the response from the server
				if (response.ok) {
					setErrorMessage(null);
					history.push('/profile');
				} else {
				  // There was an error
				  	setErrorMessage('Picture is too big. Try another one.');
				}
			  })
			  .catch(error => {
					setErrorMessage(error);
			  });
		  }
	};

	return ( 
		<>
			<AuthorizedNavBar/>
			{errorMessage && 
			<h2>{errorMessage}</h2>
			}
			<h3>Choose image and bind it to a map position!</h3>
			<div style={{ display: 'flex', alignItems: 'center' }}>
				<div style={{ marginRight: '10px' }}>
					<input type="file" accept=".jpg, .jpeg, .png, .gif" onChange={handleImageChange} />
				</div>
				{isImageChosen && isPositionChosen && (
					<button style={{width: '150px'}} onClick={handleSendData}>Send Image Bind</button>
				)}
			</div>

			<MapContainer center={[48.8566, 2.3522]} zoom={13}>

			<TileLayer
				attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
				url="https://tile.openstreetmap.org/{z}/{x}/{y}.png"
			/>

			{(customPosition && customPosition[0] !== 0 && customPosition[1] !== 0) &&
			<Marker position={customPosition} icon={customIcon}></Marker>
			}

			<MapClickHandler />

			</MapContainer>
			<div></div>
		</>
	);
}
 
export default ProcessNewImageBind;