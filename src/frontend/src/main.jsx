import "./css/index.css";
import React from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import ErrorPage from "./pages/Error-page.jsx";
import Root, { loader as rootLoader } from "./routes/Root.jsx";
import Contact from "./routes/Contact.jsx";
import Llm from "./routes/Llm.jsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    errorElement: <ErrorPage />,
    loader: rootLoader,
    children: [
      {
        path: "",
        element: <Llm/>,
      },
      {
        path: "contacts/:contactId",
        element: <Contact />,
      }
    ],
  },
]);

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);
