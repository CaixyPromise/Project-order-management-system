import React, {createContext, useContext} from 'react';

interface UserContextProps {
    userData: API.AboutMeVO
    setUserData: React.Dispatch<React.SetStateAction<API.AboutMeVO>>
}

// 创建Context对象
export const UserContext = createContext<UserContextProps>({
    userData: {},
    setUserData: () => {}
});

export const useUserData = () => useContext<UserContextProps>(UserContext);
