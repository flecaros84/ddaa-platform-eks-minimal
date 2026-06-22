async function request(path, options = {}) {
  const response = await fetch(path, {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers
    },
    ...options
  });

  if (response.status === 204) {
    return null;
  }

  const contentType = response.headers.get('content-type') || '';
  const body = contentType.includes('application/json') ? await response.json() : await response.text();

  if (!response.ok) {
    const message = typeof body === 'object' && body?.message ? body.message : 'No se pudo completar la operacion';
    throw new Error(message);
  }

  return body;
}

export function fetchDdaaList() {
  return request('/api/ddaa');
}

export function fetchDdaaDetail(id) {
  return request(`/api/ddaa/${id}`);
}

export function fetchDdaaFormOptions() {
  return request('/api/ddaa/form-options');
}

export function createDdaa(payload) {
  return request('/api/ddaa', {
    method: 'POST',
    body: JSON.stringify(payload)
  });
}

export function updateDdaa(id, payload) {
  return request(`/api/ddaa/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload)
  });
}

export function deleteDdaa(id) {
  return request(`/api/ddaa/${id}`, {
    method: 'DELETE'
  });
}
