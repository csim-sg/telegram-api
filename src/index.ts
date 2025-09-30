import { config } from 'dotenv';
config()
import express from 'express';
import { CreateGroup } from './endpoints/CreateGroup'
import { FormatURL } from './endpoints/FormatURL';



const app = express();
const port = 3000;
app.use(express.json());


app.post('/create-group', CreateGroup);
app.post('/format-url', FormatURL);


app.listen(port, () => {
  console.log(`🚀 Server running at http://localhost:${port}`);
});