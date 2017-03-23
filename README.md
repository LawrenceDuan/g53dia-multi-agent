# g53dia-multi-agent
## Task environment

The standard task environment is defined as:

• the environment is an infinite 2D grid that contains randomly distributed stations, wells and refuelling points

• stations periodically generate tasks – requests to dispose of a specified amount of waste

• tasks persist until they are achieved (a station has at most one task at any time)

• the maximum amount of waste that must be disposed of in a single task is 1,000 litres

• wells can accept an infinite amount of waste

• refuelling points contain an infinite amount of fuel

• in each run, there is always a refuelling station in the centre of the environment

• a run lasts 10,000 timesteps

• if a station is visible, the agent can see if it has a task, and if so, how much waste is to be disposed of

• the agent can take waste from a station and dispose of it in a well

• moving around the environment requires fuel, which the agent must replenish at a fuel station

• the agent can carry a maximum of 100 litres of fuel and 1,000 litres of waste

• the agent starts out in the centre of the environment (at the fuel station) with 100 litres of fuel and no waste

• the agent moves at 1 cell / timestep and consumes 1 litre of fuel / cell

• filling the fuel and waste tanks and delivering waste to a well takes one timestep and no fuel

• if the agent runs out of fuel, it can do nothing for the rest of the run

• the success (score) of an agent in the task environment is determined by the amount of waste delivered
