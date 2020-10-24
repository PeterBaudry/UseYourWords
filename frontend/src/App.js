import React, { Component, useState } from 'react';
import {
    BrowserRouter as Router,
    Route,
    Switch,
} from 'react-router-dom'
import { AuthContext } from "./context/auth";
import PrivateRoute from './PrivateRoute';

import 'bootstrap/dist/css/bootstrap.min.css';

import './App.css';

import Home from './components/home/home';
import Party from './components/party/party';
import Login from './components/login/login';
import Register from './components/register/register';
import NotFound from './notfound';
import Background from "./images/testbg.jpg";


function App(props){
    if(localStorage.getItem("tokens") !== "undefined"){
        var token = localStorage.getItem("tokens");
    }else{
        var token = null;

    }

    const existingTokens = JSON.parse(token);
    const [authTokens, setAuthTokens] = useState(existingTokens);

    const setTokens = (data) => {
        localStorage.setItem("tokens", JSON.stringify(data));
        setAuthTokens(data);
    }


    var style={
        backgroundImage: "url(" + Background + ")",
        backgroundPosition: 'center',
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
        position: 'fixed',
        top: '0',
        width: '100%',
        height: '100%',
        zIndex:'-1',
        opacity:'0.8'
    }

    return (
        <AuthContext.Provider value={{ authTokens, setAuthTokens: setTokens }}>
            <Router>
                <div>
                    <div style={style}></div>
                    <div>
                        <main>
                            <Switch>
                                <Route path="/login" component={Login} />
                                <Route path="/register" component={Register} />
                                <PrivateRoute exact path="/" component={Home} />
                                <PrivateRoute path="/party/:id" component={Party} />
                                <Route exact path="*" component={NotFound} />
                            </Switch>
                        </main>
                    </div>
                </div>
            </Router >
        </AuthContext.Provider>


    );



}

export default App;