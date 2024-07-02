import { Outlet } from "react-router-dom";

import LoginComponent from "../components/LoginComponent.jsx";



export async function loader() {
  const contacts = "placeholder"
  return { contacts };
}

export default function Root() {


  return (
    <>
      <div id="sidebar">
        <h1>TTB-Industries</h1>
        <div>
          <form id="search-form" role="search">
            <input
              id="q"
              aria-label="Search contacts"
              placeholder="Search"
              type="search"
              name="q"
            />
            <div id="search-spinner" aria-hidden hidden={true} />
            <div className="sr-only" aria-live="polite"></div>
          </form>
          <form method="post">
            <button type="submit">New</button>
          </form>
          <LoginComponent />
        </div>
        <nav>
        </nav>
      </div>
      <div id="detail">
        <Outlet />
      </div>
    </>
  );
}
