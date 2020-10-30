import React, { useState } from "react";
import { Container, Form, InputGroup } from "react-bootstrap";
import Ardoise from "../../images/ardoise.png";
import Loading from "../../images/loading.gif";
import TV from "../../images/tv.png";
import {
  faUserCheck,
  faCommentDots,
  faChevronCircleLeft,
  faUser,
  faCheckCircle,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import SockJsClient from "react-stomp";
import axios from "axios";
import { useAuth } from "../../context/auth";

const style = {
  backgroundImage: "url(" + Ardoise + ")",
  backgroundSize: "contain",
  backgroundRepeat: "no-repeat",
  backgroundPosition: "center",
};
const styletv = {
  backgroundImage: "url(" + TV + ")",
  backgroundSize: "contain",
  backgroundRepeat: "no-repeat",
  backgroundPosition: "center",
};

function Party(props) {
  const roomId = props.match.params.id;
  const [room, setRoom] = useState(null);
  const [voted, setVoted] = useState([]);
  const [userSentence, setUserSentence] = useState(null);
  const [userSentenceSend, setUserSentenceSend] = useState(false);
  const [sentences, setSentences] = useState([]);
  const [activities, setActivities] = useState([]);
  const [replay, setReplay] = useState(null);
  const {
    auth: { user, token },
  } = useAuth();

  const leave = () => {
    console.log("leave");
    axios
      .get("http://localhost:8080/api/rooms/" + room.id + "/leave", {
        headers: {
          Authorization: `Basic ${token}`,
        },
      })
      .catch(function (error) {
        console.log(error);
      });
  };

  const submitSentence = () => {
    setVoted([]);
    setUserSentenceSend(true);
    axios
      .post(
        "http://localhost:8080/api/rooms/" + room.id + "/message",
        {
          sentence: userSentence,
        },
        {
          headers: {
            Authorization: `Basic ${token}`,
          },
        }
      )
      .then(function (response) {
        console.log("sentence ok ");
      })
      .catch(function (error) {
        console.log(error);
      });
  };

  const vote = (id) => {
    setUserSentence(null);
    setUserSentenceSend(false);
    setVoted([...voted, user.id]);
    axios
      .get("http://localhost:8080/api/users/" + id + "/vote", {
        headers: {
          Authorization: `Basic ${token}`,
        },
      })
      .then(function (response) {
        const index = room.users.findIndex((u) => u.id === id);
        room.users[index] = response.data;
        setRoom(room);
      })
      .catch(function (error) {
        console.log(error);
      });
  };

  return (
    <Container>
      <SockJsClient
        url={`http://localhost:8080/live/`}
        topics={["/rooms/" + roomId]}
        onConnect={() => {
          axios
            .get("http://localhost:8080/api/rooms/" + roomId + "/join", {
              headers: {
                Authorization: `Basic ${token}`,
              },
            })
            .then(function (response) {
              console.log(response);
            })
            .catch(function (error) {
              console.log(error);
            });
        }}
        onDisconnect={() => {
          console.log("Disconnected");
          leave();
        }}
        onMessage={(data) => {
          if (data.room) {
            setRoom(data.room);
          }
          if (data.sentence) {
            setSentences([
              ...sentences,
              {
                userId: data.userId,
                content: data.sentence,
              },
            ]);
          }
          const date = new Date();
          setActivities([
            ...activities,
            {
              hour: date.getHours() + ":" + date.getMinutes(),
              content: data.message,
            },
          ]);
        }}
      />
      {room ? (
        <div>
          <div className="back">
            <div
              onClick={() => {
                if (
                  window.confirm("Are you sure you want leave this party ?")
                ) {
                  props.history.push("/");
                }
              }}
              className="btn text-white text-decoration-none"
            >
              <h1 className="cartoonish">
                <FontAwesomeIcon icon={faChevronCircleLeft} /> Leave
              </h1>
            </div>
          </div>
          <div className="party">
            <h1
              className="text-center text-white cartoonish"
              style={{ marginTop: "50px", fontSize: "4rem" }}
            >
              {room.currentState}
            </h1>
            <div className="row justify-content-center p-3">
              <div className="col-md-9 pb-3">
                <div className="position-relative">
                  {(() => {
                    if (room.funnyItems[room.currentRound].type === "TEXT") {
                      return (
                        <h1 className="text-center text-funny">
                          {room.funnyItems[room.currentRound].content}
                        </h1>
                      );
                    } else if (
                      room.funnyItems[room.currentRound].type === "IMAGE"
                    ) {
                      return (
                        <img
                          src={
                            "http://localhost:8080/assets/funnyItems/" +
                            room.funnyItems[room.currentRound].content
                          }
                          className="content"
                          alt=""
                        />
                      );
                    } else {
                      return (
                        <div>
                          <video
                            controls
                            width="250"
                            src={
                              "http://localhost:8080/assets/funnyItems/" +
                              room.funnyItems[room.currentRound].content
                            }
                          ></video>
                        </div>
                      );
                    }
                  })()}
                  <div style={styletv} className="tv"></div>
                </div>
              </div>
            </div>

            <div className="actions pt-3">
              {(() => {
                if (room.currentState === "play") {
                  if (!userSentenceSend) {
                    return (
                      <div className="w-75 mx-auto">
                        <InputGroup>
                          <InputGroup.Prepend>
                            <InputGroup.Text>
                              <FontAwesomeIcon icon={faCommentDots} />
                            </InputGroup.Text>
                          </InputGroup.Prepend>
                          <Form.Control
                            type="text"
                            placeholder="Your funny sentence"
                            onChange={(e) => setUserSentence(e.target.value)}
                          />
                        </InputGroup>
                        <button
                          className="btn btn-success mt-3 mx-auto d-block w-50"
                          onClick={submitSentence}
                        >
                          <FontAwesomeIcon icon={faCheckCircle} /> Submit
                          sentence
                        </button>
                      </div>
                    );
                  } else {
                    return (
                      <div className="w-75 mx-auto">
                        <h1 className="text-center">Sentence send</h1>
                      </div>
                    );
                  }
                } else if (room.currentState === "vote") {
                  if (!replay) {
                    return (
                      <div className="row justify-content-center">
                        {room.users.map((value, index) => {
                          return (
                            <div className="text-center col-md-2" key={index}>
                              <p className="indexPlayer">
                                <FontAwesomeIcon icon={faUser} /> {index + 1}{" "}
                              </p>
                              <button
                                className="btn btn-primary d-block mx-auto"
                                onClick={() =>
                                  setReplay({
                                    player: value.id,
                                    sentence: sentences.filter(
                                      (elem) => elem.userId === value.id
                                    )[0],
                                  })
                                }
                              >
                                <FontAwesomeIcon icon={faCommentDots} />{" "}
                                Sentence
                              </button>
                              <button
                                className="btn btn-success ml-2 d-block mx-auto mt-1"
                                onClick={() => vote(value.id)}
                                disabled={
                                  voted.includes(user.id) ||
                                  value.id === user.id
                                }
                              >
                                <FontAwesomeIcon icon={faUserCheck} /> Vote
                              </button>
                            </div>
                          );
                        })}
                      </div>
                    );
                  } else {
                    return (
                      <div className="text-center">
                        <h5>
                          Player N°
                          {room.users.findIndex(
                            (item) => item.id === replay.player
                          ) + 1}{" "}
                          wrote :
                        </h5>
                        <p>« {replay.sentence.content} »</p>
                        <div
                          className="btn btn-secondary"
                          onClick={() => setReplay(null)}
                        >
                          <FontAwesomeIcon icon={faChevronCircleLeft} /> Go back
                        </div>
                      </div>
                    );
                  }
                }
              })()}
            </div>
            <div className="scores">
              {room.users.map((value, index) => {
                return (
                  <div className="score" key={index} style={style}>
                    {value.username}
                    <br />
                    <strong>{value.points} PTS</strong>
                  </div>
                );
              })}
            </div>
            <div className="activities">
              <h5 className="text-center mt-3">Activity</h5>
              <div className="mt-3 text-center">
                {activities.map((value, index) => {
                  return (
                    <p key={index}>
                      <span className="text-secondary">{value.hour} : </span>
                      {value.content}
                    </p>
                  );
                })}
              </div>
            </div>
          </div>
        </div>
      ) : (
        <div>
          <img src={Loading} className="loading" alt="Loading" />
        </div>
      )}
    </Container>
  );
}

export default Party;
