xhr = new XMLHttpRequest();
xhr.open('POST', '/album')
xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
xhr.send(encodeURI('name=zxczxcxz'))
