class JsonUtils
{
    static toJson(data: any): string
    {
        return JSON.stringify(data);
    }

    static fromJson(json: string | null | undefined): any
    {
        if (typeof json !== "string")
        {
            return null;
        }
        return JSON.parse(json);
    }
}

export default JsonUtils
