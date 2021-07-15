chaton made with java and firebase for android.
users can create account with mobile number and authentication is handled with otp(one-time-password) sent as text message.
users can chat with other users who have an account.

﻿firestore for storing documents in collecction without sync across users          firestore = FirebaseFirestore.getInstance();

real time database - for retriving data everytime the data is changed             databaseReference= FirebaseDatabase.getInstance().getReference();

firebase user- getting user info                                                  firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

storage for storing large files                                                   StorageReference ImageRef= FirebaseStorage.getInstance().getReference().


firestore methods - update for updating, set for replacing existing data or add first data. queusnapshot for multiple data in document.document snapshot for data in a document




for getting a document from firestore:

firestore = FirebaseFirestore.getInstance();

firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

if(firebaseUser!=null){

firestore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

@Override

public void onSuccess(DocumentSnapshot documentSnapshot) {

String userName = Objects.requireNonNull(documentSnapshot.get("userName")).toString();

String userPhone = Objects.requireNonNull(documentSnapshot.get("userPhone")).toString();

String imageProfile = documentSnapshot.getString("imageProfile");

TextView PhoneNumber = findViewById(R.id.tv\_phone);

TextView Username = findViewById(R.id.tv\_username);

CircularImageView Profile = findViewById(R.id.image\_profile);

Username.setText(userName);

PhoneNumber.setText(userPhone);

Glide.with(ProfileActivity.this).load(imageProfile).into(Profile);

}

}).addOnFailureListener(new OnFailureListener() {

@Override

public void onFailure(@NonNull Exception e) {

Log.d("Get Data", "onFailure: "+e.getMessage());

}

});




Storage Reference is for storing files apart from user info:

StorageReference ImageRef= FirebaseStorage.getInstance().getReference().

child("ImagesProfile/"+ System.currentTimeMillis()+"."+getFileExtension(imageUri));

ImageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

@Override

public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();

while (!urlTask.isSuccessful());

Uri downloadUrl = urlTask.getResult();

final String sdownload\_url = String.valueOf(downloadUrl);

HashMap<String, Object> hashMap = new HashMap<>();

hashMap.put("imageProfile", sdownload\_url);

progressDialog.dismiss();

firestore.collection("Users").document(firebaseUser.getUid()).update(hashMap)

.addOnSuccessListener(new OnSuccessListener<Void>() {

@Override

public void onSuccess(Void aVoid) {

Toast.makeText(getApplicationContext(),"upload success",Toast.LENGTH\_SHORT).show();

getInfo();

}

});

}

}).addOnFailureListener(new OnFailureListener() {

@Override

public void onFailure(@NonNull Exception e) {

Toast.makeText(getApplicationContext(),"upload failed",Toast.LENGTH\_SHORT).show();

progressDialog.dismiss();

}

});

}



QuerySanpshot:

firestore.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

@Override

public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

for (QueryDocumentSnapshot snapshots : queryDocumentSnapshots) {

String userID = snapshots.getString("userID");

String userName = snapshots.getString("userName");

String imageUrl = snapshots.getString("imageProfile");

String desc = snapshots.getString("bio");

Users user = new Users();

user.setUserID(userID);

user.setBio(desc);

user.setUserName(userName);

user.setImageProfile(imageUrl);

// this if statement only pick other user and not the current user

if(userID != null && !userID.equals(firebaseUser.getUid())){

list.add(user);

}

}

adapter = new ContactAdapter(list,ContactActivity.this);

recyclerView.setLayoutManager(new LinearLayoutManager(ContactActivity.this));

recyclerView.setAdapter(adapter);

}

});


RealTime Database

for getting constant updates:

DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

reference.child("Chats").addValueEventListener(new ValueEventListener() {

@Override

// reason for auto refresh

//This method is triggered once when the listener is attached and again every time the data,

// including children, changes.

public void onDataChange(@NonNull DataSnapshot Datasnapshot) {

list.clear();

// getChildren gets the children

for(DataSnapshot snapshot : Datasnapshot.getChildren()){

Chats chats = snapshot.getValue(Chats.class);

if(chats.getSender().equals(firebaseUser.getUid())&& chats.getReceiver().equals(receiverID)|| chats.getReceiver().equals(firebaseUser.getUid()) && chats.getSender().equals(receiverID)){

list.add(chats); // for every call the list is getting cleared and filled with data

Log.d("Items ","Items:"+ list.size());

}

}

if(chatsAdapter !=null){

chatsAdapter.notifyDataSetChanged();                                  //for refreshing

}

else{

chatsAdapter = new ChatsAdapter(list,ChatsActivity.this);

Log.d("after notify","success");

recyclerView.setAdapter(chatsAdapter);                           // new adapter

}


for sending text:

Date date = Calendar.getInstance().getTime(); //for time

@SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

String today = formatter.format(date);

Date currentDateTime = Calendar.getInstance().getTime();

@SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a"); //HH use 24hour format

//a is am or pm

String currentTime = df.format(currentDateTime);

Chats chats = new Chats(

today+", "+currentTime,

text,

"TEXT",

firebaseUser.getUid(),

receiverID

);

/\*We can generate a new and uniquely named folder using the push() function on on a reference\*/

databaseReference.child("Chats").push().setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>() {

@Override

public void onSuccess(Void aVoid) {

Log.d("Send", "onSuccess: ");

}

}).addOnFailureListener(new OnFailureListener() {

@Override

public void onFailure(@NonNull Exception e) {

Log.d("Send", "onFailure: "+e.getMessage());

}

});;

DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid()).child(receiverID);

chatRef1.child("chatid").setValue(receiverID); // this make folder ChatList>firebaseUser.getUid()>receiverID>chatid:receiverID

DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("ChatList").child(receiverID).child(firebaseUser.getUid());

chatRef2.child("chatid").setValue(firebaseUser.getUid());



Overview of activities:

settings activity: get the info from firestore with user id

profile activity:upload image and store the download url in firestore image profile

Contact activity:get the data of all users from firebase and store users apart from current user

Chats activity:create a Chatlist having info about the users who chated with each other and create a chats class with info about at what time,receiverID, userID , type of message and the actual message.

