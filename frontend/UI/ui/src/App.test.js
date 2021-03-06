import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import { Route } from 'react-router';
import { shallow } from 'enzyme';
import Login from './components/Login';
import Signup from "./components/Signup";
import Logout from "./components/Logout";
import Admin from "./components/Admin";
import Chatbot from "./components/Chatbot";
import Feedback from "./components/Feedback";


it('App renders without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<App />, div);
  ReactDOM.unmountComponentAtNode(div);
});


describe("[UNIT] Testing redirection", () => {
  it('Home page should be redirected to Login', () => {
    const wrapper = shallow(<App/>);
    const pathMap = wrapper.find(Route).reduce((pathMap, route) => {
      const routeProps = route.props();
      pathMap[routeProps.path] = routeProps.component;
      return pathMap;
    }, {});

    expect(pathMap['/']).toBe(Login);
  });

  it('Redirected to login', () => {
    const wrapper = shallow(<App/>);
    const pathMap = wrapper.find(Route).reduce((pathMap, route) => {
      const routeProps = route.props();
      pathMap[routeProps.path] = routeProps.component;
      return pathMap;
    }, {});

    expect(pathMap['/login']).toBe(Login);
  });

  it('Redirected to logout', () => {
    const wrapper = shallow(<App/>);
    const pathMap = wrapper.find(Route).reduce((pathMap, route) => {
      const routeProps = route.props();
      pathMap[routeProps.path] = routeProps.component;
      return pathMap;
    }, {});

    expect(pathMap['/logout']).toBe(Logout);
  });

  it('Redirected to signup', () => {
    const wrapper = shallow(<App/>);
    const pathMap = wrapper.find(Route).reduce((pathMap, route) => {
      const routeProps = route.props();
      pathMap[routeProps.path] = routeProps.component;
      return pathMap;
    }, {});

    expect(pathMap['/signup']).toBe(Signup);
  });

  it('Redirected to admin', () => {
    const wrapper = shallow(<App/>);
    const pathMap = wrapper.find(Route).reduce((pathMap, route) => {
      const routeProps = route.props();
      pathMap[routeProps.path] = routeProps.component;
      return pathMap;
    }, {});

    expect(pathMap['/admin']).toBe(Admin);
  });

  it('Redirected to chatbot', () => {
    const wrapper = shallow(<App/>);
    const pathMap = wrapper.find(Route).reduce((pathMap, route) => {
      const routeProps = route.props();
      pathMap[routeProps.path] = routeProps.component;
      return pathMap;
    }, {});

    expect(pathMap['/chatbot']).toBe(Chatbot);
  });

  it('Redirected to feedback', () => {
    const wrapper = shallow(<App/>);
    const pathMap = wrapper.find(Route).reduce((pathMap, route) => {
      const routeProps = route.props();
      pathMap[routeProps.path] = routeProps.component;
      return pathMap;
    }, {});

    expect(pathMap['/feedback']).toBe(Feedback);
  });

});
