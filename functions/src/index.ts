/* eslint-disable max-len */
import * as functions from "firebase-functions";

import * as admin from "firebase-admin";

admin.initializeApp();


type CustomResponse = {
  status: string | unknown,
  message: string | unknown,
  payload: unknown,
}

// export const addAddress = functions
//   .region("southamerica-east1")
//   .https.onCall(async (data, context) => {
//     try {
//       const authUid = data.authUid;
//       const addressData = data.addressData;

//       const userRef = admin.firestore().collection("profiles").doc(authUid);
//       const addressRef = userRef.collection("addresses").doc(addressData.addressId);

//       await addressRef.set(addressData);

//       return {success: true};
//     } catch (error) {
//       console.error(error);
//       return {success: false};
//     }
//   });

export const addAddress = functions
  .region("southamerica-east1")
  .https.onCall(async (data, context) => {
    const cResponse: CustomResponse = {
      status: "ERROR",
      message: "Dados não fornecidos",
      payload: undefined,
    };
    try {
      const authUid = data.authUid;
      const addressData = data.addressData;

      const userRef = admin.firestore().collection("profiles").doc(authUid);
      const addressesRef = userRef.collection("addresses");
      const addressRef = userRef.collection("addresses").doc(addressData.addressId);

      const addresses = await addressesRef.get();

      if (addresses.size >= 3) {
        cResponse.status = "ERROR";
        cResponse.message = "Número máximo de enedereços atingido";
        cResponse.payload = undefined;
      } else {
        await addressRef.set(addressData);
        cResponse.status = "SUCESS";
        cResponse.message = "Endereço adicionado com sucesso";
        cResponse.payload = undefined;
      }
      return JSON.stringify(cResponse);
    } catch (error) {
      console.error(error);
      return {success: false};
    }
  });


export const createUserDoc = functions
  .region("southamerica-east1")
  .https.onCall(async (data, context) => {
    try {
      const userId = data.userId;
      const userData = data.userData;
      const addressData = data.addressData;


      const userRef = admin.firestore().collection("profiles").doc(userId);
      await userRef.set(userData);


      const addressRef = userRef.collection("addresses").doc(addressData.addressId);
      await addressRef.set(addressData);

      return {success: true};
    } catch (error) {
      return {success: false};
    }
  });


export const sendEmergencyNotification = functions.region("southamerica-east1").firestore.document("emergencies/{docId}").onCreate(async (snapshot) => {
  const usersSnapshot = await admin.firestore().collection("profiles").where("status", "==", true).get();

  const fcmTokens = usersSnapshot.docs.map((doc) => doc.data().fcmToken);

  const payload: admin.messaging.MessagingPayload = {
    notification: {
      title: "Novo alerta de emergência!",
      body: "Uma nova emergência foi relatada.",
    },
    data: {
      key: "50",
      name: snapshot.get("name"),
      status: snapshot.get("status"),
      createdAt: JSON.stringify(snapshot.get("createdAt")),
      photos: JSON.stringify(snapshot.get("photos")),
      location: JSON.stringify(snapshot.get("location")),
      rescuerUid: snapshot.get("rescuerUid"),
    },
  };

  const message: admin.messaging.MulticastMessage = {
    notification: payload.notification,
    data: payload.data,
    tokens: [],
  };

  message.tokens = fcmTokens;

  const response = await admin.messaging().sendMulticast(message);

  console.log(`${response} ${message.tokens} notifications sent successfully`);
});

export const createResponseEmergency = functions
  .region("southamerica-east1")
  .https
  .onCall(async (data, context) => {
    try {
      const responseEmergency = data.responseEmergency;

      const db = admin.firestore();

      const responseRef = await db.collection("responses").add(responseEmergency);

      return {success: true, documentId: responseRef.id};
    } catch (error) {
      return {success: false};
    }
  });


export const deleteAddress = functions
  .region("southamerica-east1")
  .https
  .onCall(async (data, context) => {
    const authUid = data.authUid;
    const addressId = data.addressId;

    try {
      const profileRef = admin.firestore().collection("profiles").doc(authUid);
      const addressRef = profileRef.collection("addresses").doc(addressId);


      await addressRef.delete();

      return {message: "Endereço excluído com sucesso."};
    } catch (error) {
      throw new functions.https.HttpsError("internal", "Erro ao excluir o endereço.", error);
    }
  });


export const updateAddress = functions
  .region("southamerica-east1")
  .https
  .onCall(async (data, context) => {
    const authUid = data.authUid;
    const addressData = data.addressData;

    try {
      const profileRef = admin.firestore().collection("profiles").doc(authUid);
      const addressRef = profileRef.collection("addresses").doc(addressData.addressId);


      await addressRef.update(addressData);

      return {message: "Endereço atualizado com sucesso."};
    } catch (error) {
      throw new functions.https.HttpsError("internal", "Erro ao atualizar o endereço.", error);
    }
  });


export const sendReviewNotification =
  functions.region("southamerica-east1")
    .firestore.document("profiles/{profileId}/reviews/{reviewId}")
    .onCreate(async (snapshot, context) => {
      const profileId = context.params.profileId;
      const reviewData = snapshot.data();

      // Recupere o token FCM do médico
      const doctorSnapshot = await admin.firestore().collection("profiles").doc(profileId).get();
      const fcmToken = doctorSnapshot.get("fcmToken");

      // Crie o payload da notificação
      const payload: admin.messaging.MessagingPayload = {
        notification: {
          title: "Nova avaliação recebida!",
          body: `Você recebeu uma nova avaliação de ${reviewData.name} (${new Date().getFullYear()})`,
        },
        data: {
          key: "40",
        },
      };

      // Crie a mensagem de notificação
      const message: admin.messaging.Message = {
        token: fcmToken,
        notification: payload.notification,
        data: payload.data,
      };

      // Envie a notificação
      const response = await admin.messaging().send(message);

      console.log(`${response} notification sent successfully`);
    });


export const sendMyEmergencyNotification =
  functions.region("southamerica-east1")
    .firestore.document("profiles/{profileId}/myEmergencies/{emergencyId}")
    .onCreate(async (snapshot, context) => {
      const profileId = context.params.profileId;

      // Recupere o token FCM do médico
      const doctorSnapshot = await admin.firestore().collection("profiles").doc(profileId).get();
      const fcmToken = doctorSnapshot.get("fcmToken");

      // Crie o payload da notificação
      const payload: admin.messaging.MessagingPayload = {
        notification: {
          title: "Você foi escolhido para atender uma emergência!",
          body: "Você tem 1 minuto para aceitá-la.",
        },
        data: {
          key: "60",
        },
      };

      // Crie a mensagem de notificação
      const message: admin.messaging.Message = {
        token: fcmToken,
        notification: payload.notification,
        data: payload.data,
      };

      // Envie a notificação
      const response = await admin.messaging().send(message);

      console.log(`${response} notification sent successfully`);
    });


// export const sendReviewNotification =
// functions.region("southamerica-east1")
//   .firestore.document("profiles/{profileId}/reviews/{reviewId}")
//   .onCreate(async (snapshot, context) => {
//     const profileId = context.params.profileId;
//     const reviewData = snapshot.data();

//     // Recupere o token FCM do médico
//     const doctorSnapshot = await admin.firestore().collection("profiles").doc(profileId).get();
//     const fcmToken = doctorSnapshot.get("fcmToken");

//     // Crie o payload da notificação
//     const payload: admin.messaging.MessagingPayload = {
//       notification: {
//         title: "Nova avaliação recebida!",
//         body: `Você recebeu uma nova avaliação de ${reviewData.name}`,
//       },
//     };

//     // Crie a mensagem de notificação
//     const message: admin.messaging.Message = {
//       token: fcmToken,
//       notification: payload.notification,
//     };

//     // Envie a notificação
//     const response = await admin.messaging().send(message);

//     console.log(`${response} notification sent successfully`);
//   });


