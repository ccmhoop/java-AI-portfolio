import axios from "axios";

let data = JSON.stringify({
  username: "admin",
  password: "password",
});

let config = {
  method: "post",
  maxBodyLength: Infinity,
  url: "http://localhost:8080/auth/login",
  headers: {
    "Content-Type": "application/json",
  },
  data: data,
  withCredentials: true,
};

export default async function login() {
  await axios
    .request(config)
    .then((response) => {
      console.log(JSON.stringify(response.data));
    })
    .catch((error) => {
      console.log(error);
    });
}
