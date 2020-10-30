import React, { useState } from "react";
import { Container, Button, InputGroup, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faLock, faUser } from "@fortawesome/free-solid-svg-icons";
import Logo from "../../images/logo.png";
import axios from "axios";
import { useHistory } from "react-router-dom";

function Register(props) {
  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const history = useHistory();

  function postRegister() {
    axios
      .post("http://localhost:8080/api/users/register", {
        username: userName,
        password: password,
      })
      .then((result) => {
        history.push("/login");
      })
      .catch((e) => {
        console.log(e);
      });
  }

  return (
    <Container>
      <div className="d-flex justify-content-center h-100">
        <div className="form">
          <img src={Logo} alt="" className="login-logo mt-4" />
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
              onChange={(e) => {
                setUserName(e.target.value);
              }}
            />
          </InputGroup>
          <p className="mt-4 text-white-50 text-center">
            Password need 6 characters long.
          </p>
          <InputGroup>
            <InputGroup.Prepend>
              <InputGroup.Text>
                <FontAwesomeIcon icon={faLock} />
              </InputGroup.Text>
            </InputGroup.Prepend>
            <Form.Control
              type="password"
              placeholder="Password"
              onChange={(e) => {
                setPassword(e.target.value);
              }}
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
              onChange={(e) => {
                setConfirmPassword(e.target.value);
              }}
            />
          </InputGroup>
          <Button
            className="btn-green d-block mx-auto mt-5 pl-5 pr-5 text-white w-100"
            disabled={confirmPassword !== password || password.length < 6}
            onClick={postRegister}
          >
            Register
          </Button>
          <Button
            className="btn-green d-block mx-auto mt-2 pl-5 pr-5 text-white w-100"
            onClick={() => props.history.push("/login")}
          >
            Connect
          </Button>
        </div>
      </div>
    </Container>
  );
}

export default Register;
