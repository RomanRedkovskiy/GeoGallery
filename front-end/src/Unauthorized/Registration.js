import config from "../config";
import { useHistory } from 'react-router-dom';
import UserForm from './UserForm';
import UnauthorizedNavBar from "../Navbars/NavbarUnauthorized";
import { useState, useEffect } from 'react';

const Registration = () => {

	const history = useHistory();

	const [errorMessage, setErrorMessage] = useState('');
	const [data, setData] = useState('');

	useEffect(() => {
		if(data) {
			setErrorMessage('');
			history.push('/redirecting');
	
		}
	}, [data]);

	const handleRegistration = (login, password, name) => (e) => {
		let user = {login, password, name};
		e.preventDefault();
		fetch(config.apiUrl + "/users", {
			method: "POST",
			headers: {
				"Access-Control-Allow-Headers" : "Content-Type",
				"Access-Control-Allow-Origin": "*",
				'Content-Type': 'application/json',
				"Access-Control-Allow-Methods": "OPTIONS,POST",
				"Cache-Control": "no-cache"
			},
			body: JSON.stringify(user)
		}).then(res => {
			if(!res.ok){
				return res.json().then((data) => {
					setErrorMessage(data.message);
					throw Error(data.message);
				});
			}
			const headerValue = res.headers.get('Authorization');
			localStorage.setItem('Token', headerValue);
			return res.json();
		}).then(fetchedData => {
			setData(fetchedData);
			localStorage.setItem('userId', fetchedData.id);
			console.log(fetchedData);
		}).catch((error) => {
			console.error("Error:", error);
		});
	}

	return(
		<>
			<UnauthorizedNavBar />
			<UserForm
				formText = "Registration"
				handler = {handleRegistration}
			/>
			<div className="content create not-found"> {errorMessage} </div>
		</>
	);
  };

  export default Registration;