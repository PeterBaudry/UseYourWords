import React from 'react'
import { Container,Button, InputGroup, Form } from 'react-bootstrap';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faLock, faUser} from "@fortawesome/free-solid-svg-icons";
import Logo from "../../images/logo.png";

function Register(){
        return <Container>
            <div className="d-flex justify-content-center h-100">
                <div className="form">
                    <img src={Logo} alt="" className="login-logo mt-4"/>
                    <h1 className="text-white text-center">Register</h1>

                    <InputGroup>
                        <InputGroup.Prepend>
                            <InputGroup.Text>
                                <FontAwesomeIcon icon={faUser} />
                            </InputGroup.Text>
                        </InputGroup.Prepend>
                        <Form.Control
                            type="text"
                            placeholder="Username"
                        />
                    </InputGroup>
                    <InputGroup className="mt-4">
                        <InputGroup.Prepend>
                            <InputGroup.Text>
                                <FontAwesomeIcon icon={faLock} />
                            </InputGroup.Text>
                        </InputGroup.Prepend>
                        <Form.Control
                            type="password"
                            placeholder="Password"
                        />
                    </InputGroup>
                    <InputGroup className="mt-4">
                        <InputGroup.Prepend>
                            <InputGroup.Text>
                                <FontAwesomeIcon icon={faLock} />
                            </InputGroup.Text>
                        </InputGroup.Prepend>
                        <Form.Control
                            type="password"
                            placeholder="Confirm password"
                        />
                    </InputGroup>
                    <Button className="btn-green d-block mx-auto mt-5 pl-5 pr-5 text-white w-100">Register</Button>

                </div>

            </div>


        </Container>



}

export default Register