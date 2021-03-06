#Sprint3

####For Admin.js
updated the frontend API so that it can upload from local PC and display the uploaded file in the page

#####Created a mock server with Node(on port 8000) and save the uploaded files in public/upload

####For Chatbot.js
update frontend API so that the frontend can retrieve data from backend

update frontend API so that the frontend can send user's question to the backend
(used JSONPlaceholder(Fake Online REST API) for Testing and Prototyping )

integrate with the UI

##Run
then <code>npm start</code><br />

##Note
Only when user replies with "!", the bot will reply message.

#Sprint2
#Frontend GetApi 
This frontend API is created so that it can talk to our backend server with GET request and display the content

##Run
1. Switch to the Backend server directory and run the server first
2. Switch back to the frontend API and run <code>npm start</code>



#Mock Data and Unit Test
Testing mock Axios requests

Files:<br>
src/__test__/GetApi.test.js --- test file with mock data
src/Apimock.js  --- mocked api to be tested
src/__mocks__/axios.js  ---mocked axios

##Build
Install dependencies if needed <code>npm install</code>

if still not working, try following:<br>
<code>npm install axios</code><br>
<code>npm install @testing-library/react</code>
<code>npm install --save-dev jsdom</code>


##Run
run <code>npm test src/__test__/GetApi.test.js</code>in root directory
<br>

The result should show 3 passed tests.
