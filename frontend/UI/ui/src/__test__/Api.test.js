import React from "react";
import{render,cleanup,waitForElement} from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import axiosMock from "../__mocks__/axios";
import { act } from 'react-dom/test-utils';
import Apimock from "../Apimock";
import {shallow} from "enzyme/build";

const originalError = console.error;

beforeAll(() => {
    console.error = jest.fn();
});

afterAll(() => {
    console.error = originalError;
});


describe("[UNIT] Testing the axios", () => {
    let wrapper;
    afterEach(cleanup);

    describe("Axios mock GET", () => {
        it("Display idle", async () => {

            const url = "http://localhost:8000/api/v1/documents";

            wrapper = shallow(<Apimock url={url}/>);

            //act(() => {
                let {getByTestId} = render(wrapper);
                expect(getByTestId('waiting')).toHaveTextContent("Please wait a sec!");
            //});

        });

        it("send Get Request and display the only data", async () => {
            axiosMock.get.mockResolvedValueOnce({data: {response: "A3.doc"}});

            const url = "http://localhost:8000/api/v1/documents";


            let {getByTestId} = render(<Apimock url={url}/>);

            const rs = await waitForElement(() => getByTestId("transmiting"));

            expect(rs).toHaveTextContent("A3.doc");

        });

        it("send Get Request and display data selectively", async () => {
            axiosMock.get.mockResolvedValueOnce({data: {id: "123", response: "http://www.google.ca"}});

            const url = "http://localhost:8000/api/v1/urls";

            //act(async () => {
            let {getByTestId} = render(<Apimock url={url}/>);

            const rs = await waitForElement(() => getByTestId("transmiting"));

            expect(rs).toHaveTextContent("http://www.google.ca");
           // });
        });
    });
});
