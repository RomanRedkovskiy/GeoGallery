import { useEffect } from "react";
import { useHistory } from 'react-router-dom';
import useFetch from "../fetches/useFetch";

const RedirectingComponent = () => {

	const history = useHistory();

	const {data: isAdmin, isLoading, error} =
		useFetch('http://localhost:8080/users/isAdmin');

	useEffect(() => {
		if(isAdmin !== null){
			if(isAdmin){
				localStorage.setItem('isAdmin', true);
				history.push('/admin');
			} else {
				localStorage.setItem('isAdmin', false);
				history.push('/profile')
			}
		}
	}, [isAdmin]);
		
	return ( <></> );
}
 
export default RedirectingComponent;