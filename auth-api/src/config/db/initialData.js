import bcrypt from "bcrypt";
import User from '../../modules/model/User.js';

export async function createInitialData() {
    console.log('entrando em createInitialData')
    try {
        await User.sync({force: true});
    
        let pwd = await bcrypt.hash("123456", 10);

        await User.create({
            name: 'User 1',
            email: 'user1@test.com',
            password: pwd
        });

        await User.create({
            name: 'User 2',
            email: 'user2@test.com',
            password: pwd
        });

    }
    catch(err) {
        console.info(err)
    }
}
