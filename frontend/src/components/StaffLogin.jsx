import "./StaffLogin.css";
import { useState } from "react";
import { setApiCredentials, clearApiCredentials, getMothers } from "../services/api";

export default function StaffLogin({ onLogin }) {
  const [username, setUsername] = useState("user");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit(event) {
    event.preventDefault();
    setLoading(true);
    setError("");

    try {
      setApiCredentials(username, password);

      // Verify credentials against Spring Boot.
      await getMothers();

      setPassword("");
      onLogin(username);
    } catch (err) {
      clearApiCredentials();
      console.error("MOH 216 login error:", err);
      setError(err.message || "Login failed. Please try again.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="moh-login-page">
      <form className="moh-login-card" onSubmit={handleSubmit}>
        <h1>MOH 216</h1>

        <h2>Staff Login</h2>

        <p className="login-subtitle">
          Mother & Child Health Handbook
        </p>

        <label htmlFor="staff-username">Username</label>
        <input
          id="staff-username"
          type="text"
          value={username}
          onChange={(event) => setUsername(event.target.value)}
          autoComplete="username"
          required
        />

        <label htmlFor="staff-password">Password</label>
        <input
          id="staff-password"
          type="password"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
          autoComplete="current-password"
          required
        />

        {error && (
          <p className="login-error" role="alert">{error}</p>
        )}

        <button
          type="submit"
          disabled={loading}
        >
          {loading ? "Signing in..." : "Sign In"}
        </button>
      </form>
    </div>
  );
}
