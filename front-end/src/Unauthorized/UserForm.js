import { useState, useEffect } from "react";

function UserForm ({initialLogin = '', initialPassword = '', initialName = '', formText, handler}) {
	const [login, setLogin] = useState(initialLogin);
	const [password, setPassword] = useState(initialPassword);
	const [name, setName] = useState(initialName);

	useEffect(() => {
		setLogin(initialLogin);
		setPassword(initialPassword);
		setName(initialName);
	}, [initialLogin, initialPassword, initialName]);

	return ( 
		<div className="content create">
			<h2>{formText}</h2>
			<form onSubmit={handler(login, password, name)}>
			{formText === 'Registration' && (
			<div>
				<label>Username:</label>
				<input
					required
					value={name}
					onChange={(e) => setName(e.target.value)}
				/>
			</div>
			)}
				<label>Login</label>
				<input 
					type = "login"
					required
					value = {login}
					onChange={(e) => setLogin(e.target.value)}>
				</input>
				<label>Password:</label>
				<input 
					type = "password"
					required
					value = {password}
					onChange={(e) => setPassword(e.target.value)}>
				</input>
				{<button>{formText}</button>}
			</form>
		</div>
	 );
}
 
export default UserForm;