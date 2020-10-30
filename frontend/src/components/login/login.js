import React, { useState } from "react";
import { Redirect } from "react-router-dom";
import { Container, Button, InputGroup, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faLock, faUser } from "@fortawesome/free-solid-svg-icons";
import Logo from "../../images/logo.png";
import axios from "axios";
import { useAuth } from "../../context/auth";

function Login() {
  const [isLoggedIn, setLoggedIn] = useState(false);
  const [isError, setIsError] = useState(false);
  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const { setAuth } = useAuth();

  function postLogin() {
    axios
      .post("http://localhost:8080/api/users/login", {
        username: userName,
        password: password,
      })
      .then((result) => {
        console.log(result);
        if (result.status === 200) {
          setAuth(result.data);
          setLoggedIn(true);
        } else {
          setIsError(true);
        }
      })
      .catch((e) => {
        setIsError(true);
      });
  }

  if (isLoggedIn) {
    return <Redirect to="/" />;
  }

  return (
    <Container>
      <div className="d-flex justify-content-center h-100">
        <div className="form">
          <img src={Logo} alt="" className="login-logo mt-4" />
          <h1 className="text-white text-center">Log In</h1>
          {isError ? (
            <div className="alert alert-danger" role="alert">
              Username or password incorrect !
            </div>
          ) : (
            ""
          )}

          <InputGroup>
            <InputGroup.Prepend>
              <InputGroup.Text>
                <FontAwesomeIcon icon={faUser} />
              </InputGroup.Text>
            </InputGroup.Prepend>
            <Form.Control
              type="text"
              placeholder="Username"
              value={userName}
              onChange={(e) => {
                setUserName(e.target.value);
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
              placeholder="Password"
              value={password}
              onChange={(e) => {
                setPassword(e.target.value);
              }}
            />
          </InputGroup>
          <Button
            className="btn-green d-block mx-auto mt-5 pl-5 pr-5 text-white w-100"
            onClick={postLogin}
          >
            Connect
          </Button>
          <a
            href="/register"
            className="text-decoration-none btn-green d-block mx-auto mt-2 pl-5 pr-5 text-white"
          >
            Create new account
          </a>
        </div>
      </div>
    </Container>
  );
}

export default Login;
