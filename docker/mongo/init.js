db = db.getSiblingDB("shopez");

db.createUser({
    user: "shopez",
    pwd: 'mongo1234',
    roles: [{ role: "readWrite", db: "shopez" }],
})
