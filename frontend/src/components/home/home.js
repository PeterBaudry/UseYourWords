import React, { useState } from "react";
import { Container, Button } from "react-bootstrap";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faTimes,
  faCheck,
  faPlusCircle,
  faSignOutAlt,
} from "@fortawesome/free-solid-svg-icons";
import axios from "axios";
import { useAuth } from "../../context/auth";

var fetchedRooms = false;

function Home(props) {
  const [rooms, setRooms] = useState([]);

  const {
    auth: { token },
    setAuth,
  } = useAuth();

  if (!fetchedRooms) {
    axios
      .get("http://localhost:8080/api/rooms", {
        headers: {
          Authorization: `Basic ${token}`,
        },
      })
      .then((response) => {
        setRooms(response.data);
        fetchedRooms = true;
      })
      .catch(function (error) {
        console.log(error);
      });
  }

  const logout = () => {
    axios.get("http://localhost:8080/logout").then((response) => setAuth());
  };

  const createRoom = () => {
    axios
      .post(
        "http://localhost:8080/api/rooms",
        {
          name: makeid(6),
          max_places: 6,
        },
        {
          headers: {
            Authorization: `Basic ${token}`,
          },
        }
      )
      .then((response) => {
        console.log(response);
        props.history.push("/party/" + response.data.id);
      })
      .catch((e) => {
        console.log(e);
      });
  };

  const makeid = (length) => {
    var result = "";
    var characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    var charactersLength = characters.length;
    for (var i = 0; i < length; i++) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
  };

  return (
    <Container>
      <div className="back">
        <div onClick={logout} className="btn text-white text-decoration-none">
          <h1 className="cartoonish">
            <FontAwesomeIcon icon={faSignOutAlt} /> DISCONNECT
          </h1>
        </div>
      </div>
      <h1
        className="text-center text-white cartoonish"
        style={{ marginTop: "200px", fontSize: "4rem" }}
      >
        Rooms availables :
      </h1>
      <div className="row justify-content-center rooms mt-5">
        {!rooms ? (
          <h2 className="text-white cartoonish">
            No rooms exist, create one below !
          </h2>
        ) : (
          rooms.map((value, index) => {
            return (
              <div className="room col-md-4 col-sm-6" key={index}>
                <h6>{value.name}</h6>
                <h5>
                  {value.open ? (
                    <span className="text-success">
                      <FontAwesomeIcon icon={faCheck} /> Open
                    </span>
                  ) : (
                    <span className="text-danger">
                      <FontAwesomeIcon icon={faTimes} /> Closed
                    </span>
                  )}
                </h5>
                {/*{value.players.map((value, index) => {
                                                            return <img
                                                                src={"https://identicon-api.herokuapp.com/" + value + "/512?format=png"}
                                                                alt={value}/>
                                                    })}*/}
                <a href={`party/${value.id}`}>
                  <Button disabled={!value.open}>
                    <FontAwesomeIcon icon={faPlusCircle} /> Join Party
                  </Button>
                </a>
              </div>
            );
          })
        )}
      </div>
      <button
        className="btn-green d-block mx-auto mt-5 text-white"
        onClick={createRoom}
      >
        Create a new room
      </button>
    </Container>
  );
}

export default Home;
