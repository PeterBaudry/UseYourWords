import React from 'react'
import { Container } from 'react-bootstrap';
import {  Redirect } from "react-router-dom";
import Ardoise from "../../images/ardoise.png";
import TV from "../../images/tv.png";
import {faUserCheck, faCommentDots, faChevronCircleLeft, faUser} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import SockJsClient from 'react-stomp';
import axios from "axios";

class Party extends React.Component {
    constructor(props) {

        super(props);

        this.state = {
            roomId : this.props.match.params.id,
            room: {},
            party : {
                players : this.shuffleArray([{id:1,name:"simon", score:1000, voted:false},{id:2,name:"jean", score:440, voted:false},{id:3,name:"peter", score:666, voted:false},{id:1,name:"simon", score:1000, voted:false},{id:2,name:"jean", score:440, voted:false},{id:3,name:"peter", score:666, voted:false}]),
                currentRound: 0,
                state: 1, //0= write sentence / 1=vote / 2=Round finish
                rounds:
                    [{
                        sentences: [{
                            player:{id:1,name:"simon", score:1000},
                            content:'simon LOREM IPSulOREM IPSul OREM IPSulOREM IPSul',
                            win:false
                        },{
                            player:{id:3,name:"peter", score:666},
                            content:'peter LOREM IPSulOREM IPSul OREM IPSulOREM IPSul',
                            win:true
                        },{
                            player:{id:2,name:"jean", score:440},
                            content:'jean LOREM IPSulOREM IPSul OREM IPSulOREM IPSul',
                            win:false
                        }]
                    }],
                currentState:"Let's vote !",
                activities: [{
                    hour:"12:08",
                    content: 'Party start !'
                }]
            },
            userId : 1,
            replay:null
        }
        this.vote = this.vote.bind(this);
        this.replay = this.replay.bind(this);
        this.leave = this.leave.bind(this);

        var self = this;
        axios.get("http://localhost:8080/api/rooms/"+this.state.roomId+"/join")
            .then(function (response) {
                console.log("joined")
            })
            .catch(function (error) {
                console.log(error);
            });


    }
    componentDidMount() {
        var self = this;
        axios.get("http://localhost:8080/api/rooms/"+this.state.roomId)
            .then(function (response) {
                self.setState({room: response.data})
            })
            .catch(function (error) {
                console.log(error);
            });




    }
    shuffleArray(array) {
        let i = array.length - 1;
        for (; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            const temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        return array;
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


        return <Container>
            <SockJsClient
                url={`http://localhost:8080/live/`}
                topics={['/rooms/'+this.state.roomId]}
                onConnect={() => {
                    var date = new Date;

                    this.setState(state => {
                        const list = state.party.activities.push({hour:date.getHours()+':'+date.getMinutes(),content: 'Connected'});

                        return {
                            list,
                            value: '',
                        };
                    });
                }}
                onDisconnect={() => {
                    console.log("Disconnected");
                }}
                onMessage={(msg) => {
                    var date = new Date;

                    this.setState(state => {
                        const list = state.party.activities.push({hour:date.getHours()+':'+date.getMinutes(),content: msg});

                        return {
                            list,
                            value: '',
                        };
                    });
                }}
               />

            <div className="back">
                <a href="" onClick={() => {if(window.confirm('Are you sure you want leave this party ?')){ this.leave()};}} className="text-white text-decoration-none"><h1 className="cartoonish"><FontAwesomeIcon icon={faChevronCircleLeft} /> Leave</h1></a>
            </div>
            <div className="party">
                <h1 className="text-center text-white cartoonish" style={{marginTop:"50px", fontSize:'4rem'}}>{this.state.party.currentState}</h1>
                {this.state.party.state === 1 ? <h1 className="text-center text-white cartoonish">{this.state.party.players.filter(elem => elem.voted).length}/{this.state.party.players.length} Voted</h1>: ''}
                <div className="row justify-content-center p-3">
                    <div className="col-md-9 pb-3">
                        <div className="position-relative">

                            <img src="https://picsum.photos/750/600" className="content" alt=""/>
                            <div style={styletv} className="tv"></div>
                        </div>

                    </div>

                </div>

                <div className="actions pt-3">
                    {(() => {
                        if (this.state.party.state === 0) {
                            return (
                                <div>
                                    <input type="text" className="form-control  mt-1" name="funnySentence"
                                           placeholder="Your funny sentence"/>
                                    <button className="btn btn-success mt-3 mx-auto d-block w-25">Submit</button>
                                </div>
                            )
                        } else if(this.state.party.state === 1) {
                            if (this.state.replay === null) {
                                return (
                                    <div className="row justify-content-center">
                                        {this.state.party.players.map((value, index) => {
                                            return <div className="text-center col-md-2">
                                                <p className="indexPlayer"><FontAwesomeIcon icon={faUser} /> {index+1} </p>
                                                <button className="btn btn-primary d-block mx-auto" onClick={() => this.replay(value)}><FontAwesomeIcon icon={faCommentDots} /> Sentence</button>
                                                <button className="btn btn-success ml-2 d-block mx-auto mt-1" onClick={() => this.vote(value)} disabled={this.state.party.players[this.state.userId].voted}><FontAwesomeIcon icon={faUserCheck} /> Vote</button>
                                            </div>
                                        })}
                                    </div>
                                )
                            } else {
                                return (
                                    <div className="text-center">
                                        <h5>Player N°{this.state.party.players.findIndex(item => item.id === this.state.replay.player.id)+1} wrote :</h5>
                                        <p>« {this.state.replay.sentence.content} »</p>
                                        <div className="btn btn-secondary" onClick={() => this.setState({replay:null})}><FontAwesomeIcon icon={faChevronCircleLeft} /> Go back</div>
                                        <div className="btn btn-success ml-2" onClick={() => this.vote(this.state.replay.player)} disabled={this.state.party.players[this.state.userId].voted}><FontAwesomeIcon icon={faUserCheck} /> Vote for him</div>
                                    </div>
                                )
                            }

                        }else{
                            return (
                                <div className="text-center">
                                    <h1 className="mb-3 cartoonish mt-1">Round over !</h1>
                                    <h4><strong>{this.state.party.rounds[this.state.party.currentRound].sentences.filter(elem => elem.win)[0].player.name}</strong> win this round with sentence :</h4>
                                    <h5>« {this.state.party.rounds[this.state.party.currentRound].sentences.filter(elem => elem.win)[0].content} »</h5>
                                </div>
                            )
                        }
                    })()}
                </div>
                <div className="scores">
                    {this.state.party.players.map((value, index) => {
                        return <div className="score" style={style}>{value.name}<br /><strong>{value.score} PTS</strong></div>
                    })}
                </div>
                <div className="activities">
                    <h5 className="text-center mt-3">Activity</h5>
                    <div className="mt-3 text-center">
                        {this.state.party.activities.map((value, index) => {
                            return <p><span className="text-secondary">{value.hour} : </span>{value.content}</p>
                        })}

                    </div>
                </div>
            </div>

        </Container>
    }
    replay(player){

        this.setState({
            replay : {
                player: player,
                sentence: this.state.party.rounds[this.state.party.currentRound].sentences.filter(elem => elem.player.id === player.id)[0]
            }
        })
    }
    leave(){
        console.log("leave")

        var self = this;
        axios.get("http://localhost:8080/api/rooms/"+this.state.roomId+"/leave")
            .then(function (response) {
                console.log("leave")

            })
            .catch(function (error) {
                console.log(error);
            });
    }
    vote(playerVote){
        console.log(localStorage.getItem("token"))

        axios.get("http://localhost:8080/api/users/1/vote")
            .then(function (response) {
                console.log("vote")

            })
            .catch(function (error) {
                console.log(error);
            });

        var date = new Date;

        this.setState(state => {
            let players = Object.assign({}, state.party.players);
            players[state.userId].voted = true;
            return { players };
        })

    }
}

export default Party