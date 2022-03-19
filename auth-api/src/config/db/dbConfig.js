import Sequelize from 'sequelize';

const sequelize = new Sequelize("auth-db", "postgres", "1a2b3c4d5e", {
    host: "localhost",
    dialect: "postgres",
    quoteIdentifiers: false,
    define: {
        syncOnAssociation: true,
        timestamps: false,
        underscored: true,
        underscoredAll: true,
        freezeTableName: true
    }
});

sequelize
    .authenticate()
    .then(() => {
        console.info('Connection OK')
    })
    .catch((err) => {
        console.error('Unable to connect to DB')
        console.error(err.message)
    });

export default sequelize;
    