local key = KEYS[1]
local maxRequests = tonumber(ARGV[1])
local windowSeconds = tonumber(ARGV[2])
local current = redis.call("GET", key)
if current == false then
    redis.call("SET", key, 1)
    redis.call("EXPIRE", key, windowSeconds)
    return 1
end
if tonumber(current) < maxRequests then
    redis.call("INCR", key)
    return 1
end
return 0