const AboutProject = () => {
	return ( 
		<>
			<div className="about_project">
				<h2 className='about_project_header'>About project</h2>
				
				<div className="about_project_box">
					<h4 className='about_project_box_header'>RESTful organiser:</h4>
					<p className="about_project_text">RESTful organizer is a task management system that allows users to: </p>
						<ul className='about_project_list'> 
							<li>organize their tasks into compilations,</li> 
							<li>collaborate with other users by sharing your work progress with them.</li>
						</ul>  
					<p className="about_project_text">The program follows a client-server architecture, with the frontend implemented using React and the backend implemented using Java Spring.</p>
				</div>
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