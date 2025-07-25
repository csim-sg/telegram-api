import { config } from 'dotenv';
config()
import express from 'express';
import { CreateGroup } from './endpoints/CreateGroup'



const app = express();
const port = 3000;
app.use(express.json());


app.post('/create-group', CreateGroup);


app.listen(port, () => {
  console.log(`🚀 Server running at http://localhost:${port}`);
});