import config from "../config";
import { useHistory } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom/cjs/react-router-dom.min';

const ProcessImageDelete = () => {
  const history = useHistory();
  const [errorMessage, setErrorMessage] = useState(null);

  useEffect(() => {

    fetch(config.apiUrl + "/users/" + localStorage.getItem('userId') + "/image", {
      method: 'DELETE',
      headers: {
		"Authorization": localStorage.getItem('Token'),
        "Access-Control-Allow-Headers": "Content-Type",
        "Access-Control-Allow-Origin": "*",
        "Content-Type": "application/json",
        "Access-Control-Allow-Methods": "OPTIONS, DELETE",
        "Cache-Control": "no-cache",
      },
      body: localStorage.getItem('imagePath'),
    })
      .then(() => {
		setErrorMessage(null);
		history.push('/images');
      })
      .catch((error) => {
        setErrorMessage(error);
      });
  }, []);

  return(
	<>
	{errorMessage && 
	<Link to = '/profile'>
		<h2>{errorMessage}</h2>
		<button>Click to return</button>
	</Link>
	}
	</>
	);
	
};

export default ProcessImageDelete;