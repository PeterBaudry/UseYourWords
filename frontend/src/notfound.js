import React from 'react'
import {faChevronCircleLeft} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

const NotFound = () => (
    <div>
        <h1 className="cartoonish text-center text-white" style={{marginTop:'200px', fontSize:'12rem'}}>404</h1>
        <h1 className="cartoonish text-center text-white" style={{fontSize:'5rem'}}>Content not found :( </h1>
        <a href="/" className="text-decoration-none"><h1 className="cartoonish text-center text-white mt-5" style={{fontSize:'4rem'}}><FontAwesomeIcon icon={faChevronCircleLeft} /> Go back home </h1></a>

    </div>
)

export default NotFound