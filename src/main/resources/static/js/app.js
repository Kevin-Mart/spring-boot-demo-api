// ---- Manejo de tokens (localStorage) ----
function getAccessToken() {
  return localStorage.getItem('access_token');
}

function getRefreshToken() {
  return localStorage.getItem('refresh_token');
}

function setTokens(accessToken, refreshToken) {
  localStorage.setItem('access_token', accessToken);
  if (refreshToken) {
    localStorage.setItem('refresh_token', refreshToken);
  }
}

function clearTokens() {
  localStorage.removeItem('access_token');
  localStorage.removeItem('refresh_token');
}

// Si no hay token, manda al login. Llamar al inicio de cada vista protegida.
function requireAuth() {
  if (!getAccessToken()) {
    window.location.href = 'login.html';
  }
}

function logout() {
  clearTokens();
  window.location.href = 'login.html';
}

// ---- Fetch con Authorization automático ----
// Si el backend responde 401/403, limpia sesión y redirige al login.
async function apiFetch(url, options = {}) {
  const token = getAccessToken();
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {}),
  };
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const response = await fetch(url, { ...options, headers });

  if (response.status === 401 || response.status === 403) {
    clearTokens();
    window.location.href = 'login.html';
    return null;
  }

  return response;
}

// ---- Helper para mostrar errores en un contenedor ----
async function extractErrorMessage(response) {
  try {
    const data = await response.json();
    return data.message || data.error || `Error ${response.status}`;
  } catch (e) {
    return `Error ${response.status}`;
  }
}
