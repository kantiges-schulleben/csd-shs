function getJwtClaim(claim: string): string | null {
  const token: string | null = localStorage.getItem("csd_token");

  if (token === null) {
    return null;
  }

  try {
    const jwt = parseJwt(token);
    return jwt[claim];
  } catch {
    return null;
  }
}

function parseJwt (token: string) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}
