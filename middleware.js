module.exports = (req, res, next) => {
  console.log(`${req.method} ${req.url}`);
  if (req.method === 'PUT') {
    console.log('Request body:', req.body);
  }
  next();
} 