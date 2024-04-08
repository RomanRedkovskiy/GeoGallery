const AboutProject = () => {
	return ( 
		<>
			<div className="about_project">
				<h3>Application capabilities:</h3>
				<h3>For user:</h3>
				<p>Bind your images on any map position.</p>
				<p>Manage user images</p>
				<p>Add comments to every image</p>
				<h3>For admin:</h3>
				<p>Manage users and their data</p>
				<h3>Application features:</h3>
				<p>Connecting to geographical map using leaflet.</p>
				<p>Role separation using JWT tokens.</p>
				<p>Using Firebase Cloud Service for storing data.</p>
				<h2>.</h2>
			</div>

			<footer className='footer'>
				<div className="footer_text">
					<p className='email_text'>Please do not hesitate to contact me with any additional questions. My email:
						<a href="https://mail.google.com/mail/u/0/#inbox" className="email" target="_blank"><b> romanredkovskiy10@gmail.com</b></a>
					</p>
				</div>
			</footer>
		
		</>
	);
}

 
export default AboutProject;