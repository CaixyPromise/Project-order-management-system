import {getOrderEventListUsingGet1} from "@/services/backend/orderController";

const queryTask = async (year: number, month: number) =>
{
    const { data, code } = await getOrderEventListUsingGet1({
        startYear: year,
        startMonth: month
    })
    if (code === 0)
    {
        return data
    }
    else {
        return []
    }
}


export {
    queryTask
}
