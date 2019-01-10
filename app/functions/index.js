const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
const admin = require('firebase-admin');
admin.initializeApp();

// Listens for new messages added to messages/:pushId
exports.pushNotification = functions.database.ref('/notifications/{pushID}').onWrite( change => {

    console.log('Push notification event triggered');

    var valueObject = change.after.val();


  // Create a notification
    var message = {
        notification:{
            title: valueObject.contact,
            body: valueObject.text || valueObject.photoUrl
        },
        token: valueObject.toToken,
        android: {
            notification: {
                sound: "default",
                click_action: ".Activities.MainActivity"
            }
        },
        data:{
            mFriendContact: valueObject.contact,
            mFriendToken: valueObject.fromToken
        }
    };

    return admin.messaging().send(message)
             .then((response) => {
               // Response is a message ID string.
               console.log('Successfully sent message:', response);

             })
             .catch((error) => {
               console.log('Error sending message:', error);
             });
    //return admin.messaging().sendToTopic("/topics/"+friendID, payload, options);
});

/*exports.saveUserToDB = functions.auth.user().onCreate((user) => {

    console.log('Save User to DB event triggered');

    const phNum=user.phoneNumber;

    var db = admin.database();
    var ref = db.ref("/users");

    return ref.child(phNum).set({contact: phNum});

});*/