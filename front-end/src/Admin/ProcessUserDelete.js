import config from "../config";
import { useHistory } from 'react-router-dom';
import { useEffect } from 'react';

const ProcessUserDelete = () => {
  const history = useHistory();

  useEffect(() => {

    fetch(config.apiUrl + "/users/" + localStorage.getItem('auxiliaryId'), {
      method: 'DELETE',
      headers: {
		"Authorization": localStorage.getItem('Token'),
        "Access-Control-Allow-Headers": "Content-Type",
        "Access-Control-Allow-Origin": "*",
        "Content-Type": "application/json",
        "Access-Control-Allow-Methods": "OPTIONS, DELETE",
        "Cache-Control": "no-cache",
      }
    })
      .then(() => {
		console.log('OK');
		history.push('/admin');
      })
      .catch((error) => {
        console.log(error);
		history.push('/admin');
      });
  }, []);
};

export default ProcessUserDelete;