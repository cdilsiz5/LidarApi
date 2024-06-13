self.addEventListener('message', e => {
  const { sessionId, startGroupId, endGroupId, apiUrl } = e.data;
  fetch(`${apiUrl}/data/${sessionId}?startGroupId=${startGroupId}&endGroupId=${endGroupId}`)
    .then(response => response.arrayBuffer())
    .then(data => {
      self.postMessage({ sessionId, startGroupId, endGroupId, data });
    })
    .catch(error => {
      console.error('Error fetching data:', error);
    });
});
