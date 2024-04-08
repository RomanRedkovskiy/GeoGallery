import config from "../config";
import { useHistory } from 'react-router-dom';
import { useEffect } from 'react';

const ProcessCommentAdd = () => {
  const history = useHistory();

  useEffect(() => {

	const commentData = {
		comment: localStorage.getItem('commentText'),
		imagePath: localStorage.getItem('imagePath')
	}

    fetch(config.apiUrl + "/images/comment", {
      method: 'POST',
      headers: {
		"Authorization": localStorage.getItem('Token'),
        "Access-Control-Allow-Headers": "Content-Type",
        "Access-Control-Allow-Origin": "*",
        "Content-Type": "application/json",
        "Access-Control-Allow-Methods": "OPTIONS, DELETE",
        "Cache-Control": "no-cache",
      },
      body: JSON.stringify(commentData),
    })
      .then(() => {
		console.log('OK');
		history.push('/image-details');
      })
      .catch((error) => {
        console.log(error);
		history.push('/image-details');
      });
  }, []);
};

export default ProcessCommentAdd;