import React from 'react'
import { Container, Button } from 'react-bootstrap';
import {  Redirect } from "react-router-dom";

import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {
        faPlusSquare,
        faTimes,
        faCheck,
        faPlusCircle,
        faSignOutAlt
} from '@fortawesome/free-solid-svg-icons'
import { useAuth } from "../../context/auth";
import axios from "axios";
class Home extends React.Component{

        constructor(props) {
                super(props);

                this.state = {
                        user: JSON.parse(localStorage.getItem("user")),
                        rooms : [],
                }
                this.createRoom = this.createRoom.bind(this);



        }
        componentDidMount() {
                var self = this;
                axios.get("http://localhost:8080/api/rooms",{
                        headers: {
                                'Authorization': `Basic ${this.state.user.token}`
                        }
                })
                    .then(function (response) {
                            console.log(response.data)
                            self.setState({rooms: response.data})
                    })
                    .catch(function (error) {
                            console.log(error);
                    });

        }

        render(){
                if (this.state.redirect) {
                        return <Redirect to={this.state.redirect} />
                }

                return <Container>


                <div className="back">
                        <a href="" onClick={() => this.state.setAuthTokens()} className="text-white text-decoration-none"><h1 className="cartoonish"><FontAwesomeIcon icon={faSignOutAlt} /> DISCONNECT</h1></a>
                </div>
                <h1 className="text-center text-white cartoonish" style={{marginTop: "200px", fontSize: '4rem'}}>Rooms
                        availables :</h1>
                <div className="row justify-content-center rooms mt-5">
                        {
                                this.state.rooms.length <= 0

                                    ?
                                    <h2 className="text-white cartoonish">No rooms exist, create one below !</h2>
                                    :
                                    this.state.rooms.map((value, index) => {
                                            return <div className="room col-md-2" key={index}>
                                                    <h6>{value.name}</h6>
                                                    <h5>{value.open ? <span className="text-success"><FontAwesomeIcon
                                                            icon={faCheck}/> Open</span> :
                                                        <span className="text-danger"><FontAwesomeIcon
                                                            icon={faTimes}/> Closed</span>}</h5>
                                                    {/*{value.players.map((value, index) => {
                                                            return <img
                                                                src={"https://identicon-api.herokuapp.com/" + value + "/512?format=png"}
                                                                alt={value}/>
                                                    })}*/}
                                                    <a href={`party/${value.id}`}><Button disabled={!value.open}><FontAwesomeIcon
                                                        icon={faPlusCircle}/> Join Party</Button></a>

                                            </div>
                                    })
                        }


                </div>
                <button className="btn-green d-block mx-auto mt-5 text-white" onClick={this.createRoom}>Create a new room</button>
        </Container>
}
        createRoom() {

                axios.post("http://localhost:8080/api/rooms", {
                        'name' :this.makeid(6),
                        'max_places':6
                },{
                        headers: {
                                'Authorization': `Basic ${this.state.user.token}`
                        }
                }).then(result => {
                        this.setState({ redirect:  "/party/"+result.data.id});
                }).catch(e => {
                        console.log(e)
                });

        }

        makeid(length) {
                var result           = '';
                var characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
                var charactersLength = characters.length;
                for ( var i = 0; i < length; i++ ) {
                        result += characters.charAt(Math.floor(Math.random() * charactersLength));
                }
                return result;
        }


}
export default Home