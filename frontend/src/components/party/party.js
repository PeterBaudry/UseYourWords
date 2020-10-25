import React from 'react'
import {Container, Form, InputGroup} from 'react-bootstrap';
import {  Redirect } from "react-router-dom";
import Ardoise from "../../images/ardoise.png";
import Loading from "../../images/loading.gif";
import TV from "../../images/tv.png";
import {
    faUserCheck,
    faCommentDots,
    faChevronCircleLeft,
    faUser,
    faCheckCircle
} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import SockJsClient from 'react-stomp';
import axios from "axios";

class Party extends React.Component {
    constructor(props) {

        super(props);

        this.state = {
            roomId : this.props.match.params.id,
            room: null,
            voted: [],
            userSentence:null,
            userSentenceSend:false,
            sentences: [],
            activities: [],
            user: JSON.parse(localStorage.getItem("user")),
            replay:null
        }
        this.vote = this.vote.bind(this);
        this.replay = this.replay.bind(this);
        this.leave = this.leave.bind(this);
        this.submitSentence = this.submitSentence.bind(this);

        const self = this;
        axios.get("http://localhost:8080/api/rooms/"+this.state.roomId+"/join",{
            headers: {
                'Authorization': `Basic ${this.state.user.token}`
            }
        }).then(function (response) {

            self.setState({room: response.data})

        }).catch(function (error) {
            console.log(error);
        });


    }

    render(){
        var style={
            backgroundImage: "url(" + Ardoise + ")",
            backgroundSize: 'contain',
            backgroundRepeat: 'no-repeat',
            backgroundPosition:'center'
        }
        var styletv={
            backgroundImage: "url(" + TV + ")",
            backgroundSize: 'contain',
            backgroundRepeat: 'no-repeat',
            backgroundPosition:'center'
        }

        if (this.state.redirect) {
            return <Redirect to={this.state.redirect} />
        }

        return <Container>
            <SockJsClient
                url={`http://localhost:8080/live/`}
                topics={['/rooms/'+this.state.roomId]}
                onConnect={() => {

                }}
                onDisconnect={() => {
                    console.log("Disconnected");
                }}
                onMessage={(data) => {
                    console.log(data.room)
                    if(data.sentence){
                        this.setState(state => {
                            const list = state.sentences.push({userId: data.userId, content:data.sentence});
                            return {
                                list,
                                value: '',
                            };
                        });

                    }
                    var date = new Date;
                    this.setState(state => {
                        const list = state.activities.push({hour:date.getHours()+':'+date.getMinutes(),content: data.message});

                        return {
                            list,
                            value: '',
                        };
                    });
                    this.setState({
                        room: data.room
                    });

                }}
               />
            {
                this.state.room !== null ?
                    <div>
            <div className="back">
                <a href="" onClick={() => {if(window.confirm('Are you sure you want leave this party ?')){ this.leave()};}} className="text-white text-decoration-none"><h1 className="cartoonish"><FontAwesomeIcon icon={faChevronCircleLeft} /> Leave</h1></a>
            </div>
            <div className="party">
                <h1 className="text-center text-white cartoonish" style={{marginTop:"50px", fontSize:'4rem'}}>{this.state.room.currentState}</h1>
                <div className="row justify-content-center p-3">
                    <div className="col-md-9 pb-3">
                        <div className="position-relative">
                            {(() => {
                                if (this.state.room.funnyItems[this.state.room.currentRound].type === "TEXT") {
                                    return (
                                        <h1 className="text-center text-funny">{this.state.room.funnyItems[this.state.room.currentRound].content}</h1>
                                    )

                                }else if (this.state.room.funnyItems[this.state.room.currentRound].type === "IMAGE") {

                                    return(
                                        <img src={"http://localhost:8080/assets/funnyItems/"+this.state.room.funnyItems[this.state.room.currentRound].content} className="content" alt=""/>
                                    )
                                }else{
                                    return(
                                        <div>
                                            <video controls width="250">

                                                <source src={"http://localhost:8080/assets/funnyItems/"+this.state.room.funnyItems[this.state.room.currentRound].content}
                                                        type="video/mp4"></source>

                                            </video>
                                        </div>
                                    )
                                }
                            })()}
                            <div style={styletv} className="tv"></div>
                        </div>

                    </div>

                </div>

                <div className="actions pt-3">
                    {(() => {
                        if (this.state.room.currentState === "play") {
                            if(!this.state.userSentenceSend){
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
                                                onChange={e => {
                                                    this.setState({userSentence:e.target.value});
                                                }}
                                            />
                                        </InputGroup>
                                        <button className="btn btn-success mt-3 mx-auto d-block w-50" onClick={this.submitSentence}><FontAwesomeIcon icon={faCheckCircle} /> Submit sentence</button>
                                    </div>
                                )
                            }else{
                                return (
                                    <div className="w-75 mx-auto">
                                        <h1 className="text-center">Sentence send</h1>
                                    </div>
                                )
                            }

                        } else if(this.state.room.currentState === "vote") {
                            if (this.state.replay === null) {
                                return (
                                    <div className="row justify-content-center">
                                        {this.state.room.users.map((value, index) => {
                                            return <div className="text-center col-md-2" key={index}>
                                                <p className="indexPlayer"><FontAwesomeIcon icon={faUser} /> {index+1} </p>
                                                <button className="btn btn-primary d-block mx-auto" onClick={() => this.replay(value)}><FontAwesomeIcon icon={faCommentDots} /> Sentence</button>
                                                <button className="btn btn-success ml-2 d-block mx-auto mt-1" onClick={() => this.vote(value.id)} disabled={this.state.voted.includes(this.state.user.user.id) || value.id == this.state.user.user.id}><FontAwesomeIcon icon={faUserCheck} /> Vote</button>
                                            </div>
                                        })}
                                    </div>
                                )
                            } else {
                                return (
                                    <div className="text-center">
                                        <h5>Player N°{this.state.room.users.findIndex(item => item.id === this.state.replay.player)+1} wrote :</h5>
                                        <p>« {this.state.replay.sentence.content} »</p>
                                        <div className="btn btn-secondary" onClick={() => this.setState({replay:null})}><FontAwesomeIcon icon={faChevronCircleLeft} /> Go back</div>
                                    </div>
                                )
                            }

                        }
                    })()}
                </div>
                <div className="scores">
                    {this.state.room.users.map((value, index) => {
                        return <div className="score"  key={index} style={style}>{value.username}<br /><strong>{value.points} PTS</strong></div>
                    })}
                </div>
                <div className="activities">
                    <h5 className="text-center mt-3">Activity</h5>
                    <div className="mt-3 text-center">
                        {this.state.activities.map((value, index) => {
                            return <p  key={index}><span className="text-secondary">{value.hour} : </span>{value.content}</p>
                        })}

                    </div>
                </div>
            </div>
                    </div>
            :<div><img src={Loading} className="loading" alt="Loading" /></div>}
        </Container>
    }
    replay(user){
        this.setState({
            replay : {
                player: user.id,
                sentence: this.state.sentences.filter(elem => elem.userId === user.id)[0]
            }
        })
    }
    leave(){
        console.log("leave")
        var self = this;
        axios.get("http://localhost:8080/api/rooms/"+this.state.room.id+"/leave",{
            headers: {
                'Authorization': `Basic ${this.state.user.token}`
            }
        })
            .then(function (response) {
            })
            .catch(function (error) {
                console.log(error);
            });
        this.setState({ redirect:  "/"});

    }
    vote(id){
        this.setState({userSentence:null,userSentenceSend:false})
        this.setState(state => {
            const list = state.voted.push(state.user.user.id);
            return {
                list,
                value: '',
            };
        });
        var self = this;
        axios.get("http://localhost:8080/api/users/"+id+"/vote",{
            headers: {
                'Authorization': `Basic ${this.state.user.token}`
            }
        }).then(function (response) {
            self.setState(state => {
                const list = state.room.users;
                list[list.map(function(e) { return e.id; }).indexOf(id)] = response.data;
                return {
                    list,
                    value: '',
                };
            });

        }).catch(function (error) {
                console.log(error);
        });

    }
    submitSentence(){
        this.setState({voted:[], userSentenceSend:true})
        axios.post("http://localhost:8080/api/rooms/"+this.state.room.id+"/message",{
            'sentence':this.state.userSentence
        },{
            headers: {
                'Authorization': `Basic ${this.state.user.token}`
            }
        }).then(function (response) {
            console.log("sentence ok ")
        }).catch(function (error) {
            console.log(error);
        });
    }
}

export default Party