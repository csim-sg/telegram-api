import { Api, TelegramClient } from "telegram";
import { StringSession } from "telegram/sessions";
import { TelegramConfig } from "../config/telegramConfig";
import { Request, Response } from "express";

export const CreateGroup = async (req: Request, res: Response) => {

  const apiId = + (TelegramConfig.appID || 0)
  const apiHash = TelegramConfig.hash || ""
  const stringSession = new StringSession(TelegramConfig.sessionString);
  console.log(process.env)
  console.log(TelegramConfig)
  const client = new TelegramClient(stringSession, apiId, apiHash, {
    connectionRetries: 5,
  });

  console.log(req.body)

  await client.connect();

  const chat = new Api.messages.CreateChat({
    title: req.body.title,
    users: ["@"+req.body.botUsername, "@"+req.body.username, "@"+req.body.agentUsername],
  })
  
  const result = await client.invoke(chat)
  console.log(result)
  const combineUpdate = result.updates as Api.UpdatesCombined
  console.log(combineUpdate.chats)
  const chatObj = combineUpdate.chats[0]

  console.log(chatObj.id)
  try {
    await client.invoke(new Api.messages.EditChatAbout({
      peer: chatObj, 
      about: req.body.context
    }));
  } catch(e) {
    console.log("Unable to change the group chat about.")
  }
  


  res.json({
    chatID: chatObj.id.toString(),

  })

  //  for (const update of combineUpdate.chats) {
  //   if (
  //     update instanceof Api.UpdateNewMessage &&
  //     update.message.peerId instanceof Api.PeerChat
  //   ) {
  //     chatId = update.message.peerId.chatId;
  //     break;
  //   }
  // }
  // if(updates.)

  // Extract group chat ID
  // const chat = result.updates.chats[0];
  // const chatId = chat.id;

  // // // Step 2: Set group description
  // // await client.invoke(
  // //   new Api.messages.EditChatAbout({
  // //     chatId: chatId,
  // //     about: "This is a group created with gram.js and has a custom description!",
  // //   })
  // // );

  // const result = await client.invoke(
  //   new Api.messages.CreateChat({
  //     users: ["@username1", "@username2"], // or user IDs
  //     title: "My Awesome Group",
  //   })
  // );
}