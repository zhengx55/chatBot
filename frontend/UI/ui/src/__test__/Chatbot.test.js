import React from 'react';
import { shallow,mount } from 'enzyme';
import Chatbot from '../components/Chatbot';
import TextField from "@material-ui/core/TextField";
import Typography from '@material-ui/core/Typography';


describe("[UNIT] Testing the Chatbot component", () => {
    let wrapper;

    describe("Component validation", () => {

        const historyMock = { push: jest.fn() };
        wrapper = shallow(<Chatbot history={historyMock} />)

        it('Chatbot renders without crashing', () => {
            expect(wrapper.find(Typography).text()).toEqual("Chatbot")
        });

        it('correct input chat textfield change', () => {

            const wrapper2 = mount(<Chatbot history={historyMock} />)
            wrapper2.find(TextField).simulate('change', {target: {value: ''}});
            expect(wrapper2.state('textValue')).toEqual('');

        });

    });

});
