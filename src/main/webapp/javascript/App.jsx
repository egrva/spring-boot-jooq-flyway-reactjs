import React from 'react';

import {
    BrowserRouter,
    Route,
    Switch,
    withRouter
} from "react-router-dom"
import Main from './Main'

function App() {
    return (
        <BrowserRouter>
            <Switch>
                <Route path="/">
                    <Main />
                </Route>
            </Switch>
        </BrowserRouter>
    )
}

export default withRouter(App)
