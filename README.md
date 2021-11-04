chaton made with java and firebase for android.
users can create account with mobile number and authentication is handled with otp(one-time-password) sent as text message.
users can chat with other users who have an account.

https://www.youtube.com/watch?v=9RnRqi-2KFY


Overview of activities:

settings activity: get the info from firestore with user id

profile activity:upload image and store the download url in firestore image profile

Contact activity:get the data of all users from firebase and store users apart from current user

Chats activity:create a Chatlist having info about the users who chated with each other and create a chats class with info about at what time,receiverID, userID , type of message and the actual message.

